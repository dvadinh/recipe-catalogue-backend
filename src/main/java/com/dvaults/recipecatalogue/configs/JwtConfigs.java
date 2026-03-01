package com.dvaults.recipecatalogue.configs;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.pem.PemContent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class JwtConfigs {

  private final Pattern PEM_KEY_HEADER_PATTERN = Pattern.compile("-+BEGIN\\s+[^-]*-+", Pattern.CASE_INSENSITIVE);
  private final Pattern PEM_KEY_FOOTER_PATTERN = Pattern.compile("-+END\\s+[^-]*-+", Pattern.CASE_INSENSITIVE);

  public static final String REFRESH_TOKEN_URI = "/auth/refresh";
  public static final String ACCESS_TOKEN_URI = "/";

  public static final String ACCESS_TOKEN_COOKIE_NAME = "JWT_ACCESS_TOKEN";
  public static final String REFRESH_TOKEN_COOKIE_NAME = "JWT_REFRESH_TOKEN";
  public static final SignatureAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.ES384;

  @Getter
  @Value("${jwt.access-token-time-to-live}")
  private long accessTokenTimeToLive;

  @Getter
  @Value("${jwt.refresh-token-time-to-live}")
  private long refreshTokenTimeToLive;

  private ECPrivateKey privateKey;
  private ECPublicKey publicKey;

  public JwtConfigs(
      @Value("${jwt.private-key-file}") Resource privateKeyResource,
      @Value("${jwt.public-key-file}") Resource publicKeyResource
  ) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    parsePrivateKeyFromPemString(privateKeyResource.getContentAsString(StandardCharsets.US_ASCII));
    parsePublicKeyFromPemString(publicKeyResource.getContentAsString(StandardCharsets.US_ASCII));
  }

  @Bean
  public ECPrivateKey jwtPrivateKey() {
    return privateKey;
  }

  @Bean
  public JwtParser jwtParser() {
    return Jwts.parser()
        .verifyWith(publicKey)
        .build();
  }

  private void parsePrivateKeyFromPemString(String pemPrivateKey) {
    PemContent privateKeyContent = PemContent.of(pemPrivateKey);
    if (privateKeyContent == null) throw new IllegalStateException("Private key could not be parsed");
    privateKey = (ECPrivateKey) privateKeyContent.getPrivateKey();
  }

  private void parsePublicKeyFromPemString(String pemPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] base64PublicKey = Base64.getDecoder()
        .decode(pemPublicKey.lines()
            .filter(line -> !PEM_KEY_HEADER_PATTERN.matcher(line).matches()
                && !PEM_KEY_FOOTER_PATTERN.matcher(line).matches())
            .collect(Collectors.joining())
            .trim());
    publicKey = (ECPublicKey) KeyFactory.getInstance("EC")
        .generatePublic(new X509EncodedKeySpec(base64PublicKey));
  }

}
