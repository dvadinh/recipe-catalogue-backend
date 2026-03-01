package com.dvaults.recipecatalogue.modules.auth.mappers;

import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.interfaces.ECPrivateKey;
import java.time.Instant;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    uses = {
        UserMapper.class,
        LinkedOAuth2AccountMapper.class
    }
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class AuthenticationMapper {

  private JwtConfigs jwtConfigs;
  private JwtParser jwtParser;
  private ECPrivateKey jwtPrivateKey;
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @Named("toUserPrincipal")
  public abstract UserPrincipal toUserPrincipal(User user);

  @Named("toUserPrincipal")
  @Mapping(target = "id", expression = "java(Long.valueOf(userJwt.getSub()))")
  @Mapping(target = "authorities", expression = "java(userJwt.getType() == null ? Set.of() : Set.of(userJwt.getType()))")
  public abstract UserPrincipal toUserPrincipal(UserJwt userJwt);

  @Named("toUserJwt")
  @Mapping(target = "sub", expression = "java(String.valueOf(userPrincipal.getId()))")
  @Mapping(target = "type", expression = "java(userPrincipal.getAuthority())")
  public abstract UserJwt toUserJwt(UserPrincipal userPrincipal);

  @Named("toUserJwt")
  public UserJwt toUserJwt(
      UserPrincipal userPrincipal,
      String jti
  ) {

    UserJwt userJwt = toUserJwt(userPrincipal);
    Instant iat = Instant.now();
    userJwt.setSub(userPrincipal.getId().toString());
    userJwt.setJti(jti);
    userJwt.setIat(iat);
    userJwt.setExp(iat.plusSeconds(jwtConfigs.getAccessTokenTimeToLive()));

    return userJwt;

  }

  @Named("toJwtString")
  public String toJwtString(
      UserPrincipal userPrincipal,
      String jti
  ) {
    return Jwts.builder()
        .claims(toUserJwt(userPrincipal, jti).toClaims())
        .signWith(jwtPrivateKey, JwtConfigs.SIGNATURE_ALGORITHM)
        .compact();
  }

  @Named("mapToUserJwt")
  public UserJwt mapToUserJwt(Claims claims) throws IllegalArgumentException {
    return objectMapper.convertValue(claims, UserJwt.class);
  }

  @Named("toUserJwt")
  public UserJwt toUserJwt(String jwtAccessToken) throws JwtException {
    try {
      return mapToUserJwt(jwtParser.parseSignedClaims(jwtAccessToken).getPayload());
    } catch (IllegalArgumentException exception) {
      throw new JwtException(exception.getMessage(), exception);
    }
  }

}
