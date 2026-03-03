package com.dvaults.recipecatalogue.modules.auth.services.api.implementation;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.BadGatewayException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ConflictException;
import com.dvaults.recipecatalogue.common.errors.exceptions.CookieAwareException;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.configs.RedisConfigs;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.UsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.AuthenticationMapper;
import com.dvaults.recipecatalogue.modules.auth.mappers.LinkedOAuth2AccountMapper;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2Account;
import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2AccountId;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.repositories.LinkedOAuth2AccountRepository;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.OAuth2SavedRequestAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final LinkedOAuth2AccountRepository linkedOAuth2AccountRepository;

  private final UserMapper userMapper;
  private final AuthenticationMapper authenticationMapper;
  private final LinkedOAuth2AccountMapper linkedOAuth2AccountMapper;

  private final Environment environment;
  private final PasswordEncoder passwordEncoder;
  private final JwtConfigs jwtConfigs;
  private final StringRedisTemplate redisTemplate;
  private final RestClient restClient;
  private final ClientRegistrationRepository clientRegistrationRepository;

  @Override
  public UserDetailsResponse findUserByPrincipal(@Nullable UserPrincipal principal) {
    return userMapper.toUserDetailsResponse(
        Optional.ofNullable(principal)
            .map(userPrincipal -> userRepository.findById(userPrincipal.getId())
                .orElse(new User()))
            .orElse(new User())
    );
  }

  @Override
  public UserPrincipal toUserPrincipal(UserJwt principalJwt) {
    return authenticationMapper.toUserPrincipal(principalJwt);
  }

  @Override
  @Transactional
  public void signUpByUsername(UsernamePasswordRequest usernamePasswordRequest) {

    UsernamePasswordRequest screenedRequest = userMapper.screenUsernamePasswordRequest(usernamePasswordRequest);

    userRepository.findByUsername(screenedRequest.username())
        .ifPresent(existingUserAccount -> {
          throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_002);
        });

    Pair<Boolean, Map<String, String>> usernamePasswordValidation = SecurityUtils.screenUsernamePassword(screenedRequest);
    if (!usernamePasswordValidation.getFirst()) {
      throw new BasicAuthenticationException(
          AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_003,
          usernamePasswordValidation.getSecond()
      );
    }

    userRepository.save(
        User.builder()
            .type(Authority.USER)
            .username(screenedRequest.username())
            .password(passwordEncoder.encode(screenedRequest.password()))
            .displayName(generateUniqueDisplayName((screenedRequest.username())))
            .enabled(true)
            .build()
    );

  }

  @Override
  @Transactional
  public UserDetailsResponse create(PostUserRequest screenedRequest) {

    Pair<Boolean, Map<String, String>> usernamePasswordValidation =
        SecurityUtils.screenUsernamePassword(
            new UsernamePasswordRequest(
                screenedRequest.username(),
                screenedRequest.password()
            )
        );
    if (!usernamePasswordValidation.getFirst()) {
      throw new RequestValidationException(
          AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_003,
          usernamePasswordValidation.getSecond()
      );
    }

    userRepository.findByUsername(screenedRequest.username())
        .ifPresent(existingUser -> {
          throw new ConflictException(AuthenticationErrorDictionary.USERNAME_ALREADY_EXISTS_001);
        });

    if (screenedRequest.displayName() != null) {
      userRepository.findByDisplayName(screenedRequest.displayName())
          .ifPresent(existingUser -> {
            throw new ConflictException(UserErrorDictionary.USER_DISPLAY_NAME_ALREADY_EXISTS_001);
          });
    }

    return userMapper.toUserDetailsResponse(
        userRepository.save(
            User.builder()
                .username(screenedRequest.username())
                .password(passwordEncoder.encode(screenedRequest.password()))
                .displayName(
                    screenedRequest.displayName() == null
                        ? generateUniqueDisplayName(screenedRequest.username())
                        : screenedRequest.displayName())
                .type(screenedRequest.type())
                .description(screenedRequest.description())
                .enabled(screenedRequest.enabled())
                .build()
        )
    );

  }

  @Override
  @Transactional
  public Long updateUsernamePassword(
      User user,
      PatchUsernamePasswordRequest screenedRequest
  ) {

    if (user.getUsername().equals(screenedRequest.username()) &&
        passwordEncoder.matches(screenedRequest.password(), user.getPassword())
    ) return null;

    if (screenedRequest.usernameOperation() == PatchRequestOperation.UPDATE
        && screenedRequest.passwordOperation() == PatchRequestOperation.UPDATE
    ) {
      Pair<Boolean, Map<String, String>> usernamePasswordValidation =
          SecurityUtils.screenUsernamePassword(
              new UsernamePasswordRequest(
                  screenedRequest.username(),
                  screenedRequest.password()
              )
          );
      if (!usernamePasswordValidation.getFirst()) {
        throw new RequestValidationException(
            AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_003,
            usernamePasswordValidation.getSecond()
        );
      }

    } else if (screenedRequest.usernameOperation() == PatchRequestOperation.UPDATE) {
      Pair<Boolean, String> usernameValidation = SecurityUtils.screenUsername(screenedRequest.username());
      if (!usernameValidation.getFirst()) {
        throw new RequestValidationException(
            AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_003,
            usernameValidation.getSecond()
        );
      }

    } else if (screenedRequest.passwordOperation() == PatchRequestOperation.UPDATE) {
      Pair<Boolean, String> passwordValidation = SecurityUtils.screenPassword(screenedRequest.password());
      if (!passwordValidation.getFirst()) {
        throw new RequestValidationException(
            AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_003,
            passwordValidation.getSecond()
        );
      }
    }

    if (screenedRequest.usernameOperation() == PatchRequestOperation.UPDATE) {
      if (!user.getUsername().equals(screenedRequest.username())) {
        userRepository.findByUsername(screenedRequest.username())
            .ifPresent(existingUser -> {
              if (!existingUser.getId().equals(user.getId())) {
                throw new ConflictException(AuthenticationErrorDictionary.USERNAME_ALREADY_EXISTS_001);
              }
            });
        user.setUsername(screenedRequest.username());
      }
    }

    if (screenedRequest.passwordOperation() == PatchRequestOperation.UPDATE) {
      user.setPassword(passwordEncoder.encode(screenedRequest.password()));
    }

    userRepository.save(user);

    return user.getId();

  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> buildJwtTokens(
      UserPrincipal principal,
      String jti
  ) {

    String refreshToken = UUID.randomUUID().toString();

    String jwtAccessToken = authenticationMapper.toJwtString(principal, jti);

    revokeJwtTokens(String.valueOf(principal.getId()));

    redisTemplate.opsForValue()
        .set(
            String.format(RedisConfigs.JWT_REFRESH_TOKEN_KEY_TEMPLATE, principal.getId(), jti),
            refreshToken,
            Duration.ofSeconds(jwtConfigs.getRefreshTokenTimeToLive())
        );

    return Pair.of(
        buildCookie(ResponseCookie.from(JwtConfigs.ACCESS_TOKEN_COOKIE_NAME)
            .value(jwtAccessToken)
            .path(JwtConfigs.ACCESS_TOKEN_URI)
            .maxAge(jwtConfigs.getAccessTokenTimeToLive())),
        buildCookie(ResponseCookie.from(JwtConfigs.REFRESH_TOKEN_COOKIE_NAME)
            .value(refreshToken)
            .path(JwtConfigs.REFRESH_TOKEN_URI)
            .maxAge(jwtConfigs.getRefreshTokenTimeToLive()))
    );

  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> buildJwtRevokingTokens() {
    return Pair.of(
        buildCookie(ResponseCookie.from(JwtConfigs.ACCESS_TOKEN_COOKIE_NAME)
            .value("")
            .path(JwtConfigs.ACCESS_TOKEN_URI)
            .maxAge(0)),
        buildCookie(ResponseCookie.from(JwtConfigs.REFRESH_TOKEN_COOKIE_NAME)
            .value("")
            .path(JwtConfigs.REFRESH_TOKEN_URI)
            .maxAge(0))
    );
  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> refreshJwtTokens(
      UserJwt principalJwt,
      String refreshToken
  ) {

    String storedRefreshToken = redisTemplate.opsForValue()
        .get(String.format(RedisConfigs.JWT_REFRESH_TOKEN_KEY_TEMPLATE, principalJwt.getSub(), principalJwt.getJti()));

    if (storedRefreshToken == null
        || !storedRefreshToken.equals(refreshToken)
    ) {
      Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = buildJwtRevokingTokens();
      throw new CookieAwareException(
          AuthenticationErrorDictionary.INVALID_JWT_REFRESH_TOKEN_001,
          List.of(jwtTokenCookies.getFirst(), jwtTokenCookies.getSecond())
      );
    }

    revokeJwtTokens(principalJwt.getSub());
    return buildJwtTokens(
        authenticationMapper.toUserPrincipal(userRepository.findById(Long.valueOf(principalJwt.getSub()))
            .orElseThrow(() -> new ResourceNotFoundException(null))),
        UUID.randomUUID().toString()
    );

  }

  @Override
  public void triggerJwtAccessTokenReset(String sub) {
    redisTemplate.opsForValue()
        .set(
            String.format(RedisConfigs.JWT_RESET_ACCESS_TOKEN_KEY_TEMPLATE, sub),
            String.valueOf(Instant.now().toEpochMilli()),
            Duration.ofSeconds(jwtConfigs.getAccessTokenTimeToLive())
        );
  }

  @Override
  public boolean isJwtAccessTokenForcedReset(String sub) {
    return redisTemplate.opsForValue()
        .get(String.format(RedisConfigs.JWT_RESET_ACCESS_TOKEN_KEY_TEMPLATE, sub)) != null;
  }

  @Override
  public boolean areValidJwtTokens(
      String sub,
      String jti
  ) {
    return StringUtils.hasText(
        redisTemplate.opsForValue()
            .get(String.format(RedisConfigs.JWT_REFRESH_TOKEN_KEY_TEMPLATE, sub, jti))
    );
  }

  @Override
  public void revokeJwtTokens(String sub) {
    Set<String> oldRefreshTokenKeys = redisTemplate.keys(String.format(RedisConfigs.JWT_REFRESH_TOKEN_KEY_TEMPLATE, sub, "*"));
    if (oldRefreshTokenKeys != null
        && !oldRefreshTokenKeys.isEmpty()
    ) {
      redisTemplate.delete(oldRefreshTokenKeys);
    }
    redisTemplate.delete(String.format(RedisConfigs.JWT_RESET_ACCESS_TOKEN_KEY_TEMPLATE, sub));
  }

  @Override
  public UserJwt parseJwtAccessToken(String jwtAccessTokenString) throws JwtException {
    return authenticationMapper.toUserJwt(jwtAccessTokenString);
  }

  @Override
  @SuppressWarnings("unchecked")
  public OAuth2AuthorizedClient loadOAuth2AuthorizedClient(
      String clientRegistrationId,
      String principalName
  ) {
    return linkedOAuth2AccountRepository.findById(new LinkedOAuth2AccountId(clientRegistrationId, principalName))
        .map(linkedOAuth2AccountMapper::toOAuth2AuthorizedClient)
        .orElse(null);
  }

  @Override
  @Transactional
  public void saveSigningUpOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {

    Optional<LinkedOAuth2Account> linkedOAuth2AccountOptional = linkedOAuth2AccountRepository.findByIdFetchUser(
        new LinkedOAuth2AccountId(
            oAuth2AuthorizedClient.getClientRegistration().getRegistrationId(),
            oAuth2AuthorizedClient.getPrincipalName()
        )
    );

    if (linkedOAuth2AccountOptional.isEmpty()) {
      LinkedOAuth2Account linkedOAuth2Account = linkedOAuth2AccountMapper.toLinkedOAuth2Account(oAuth2AuthorizedClient);
      User savedUser = userRepository.save(
          User.builder()
              .type(Authority.USER)
              .username(generateUniqueUsername(linkedOAuth2Account.getId().getPrincipalName()))
              .displayName(generateUniqueDisplayName(linkedOAuth2Account.getId().getPrincipalName()))
              .enabled(true)
              .build()
      );
      linkedOAuth2Account.setUser(savedUser);
      linkedOAuth2AccountRepository.save(linkedOAuth2Account);

    } else {
      if (linkedOAuth2AccountOptional.get().getUser() != null) {
        throw new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_OAUTH2_AUTHENTICATION_002);
      }
      LinkedOAuth2Account linkedOAuth2Account = linkedOAuth2AccountMapper.updateLinkedOAuth2Account(
          linkedOAuth2AccountOptional.get(),
          oAuth2AuthorizedClient
      );
      User savedUser = userRepository.save(
          User.builder()
              .type(Authority.USER)
              .username(generateUniqueUsername(linkedOAuth2Account.getId().getPrincipalName()))
              .displayName(generateUniqueDisplayName(linkedOAuth2Account.getId().getPrincipalName()))
              .enabled(true)
              .build()
      );
      linkedOAuth2Account.setUser(savedUser);
      linkedOAuth2AccountRepository.save(linkedOAuth2Account);
    }

  }

  @Override
  @Transactional
  public void saveSigningInOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {
    linkedOAuth2AccountRepository.save(
        linkedOAuth2AccountMapper.updateLinkedOAuth2Account(
            linkedOAuth2AccountRepository.findById(
                    new LinkedOAuth2AccountId(
                        oAuth2AuthorizedClient.getClientRegistration().getRegistrationId(),
                        oAuth2AuthorizedClient.getPrincipalName()))
                .orElseThrow(() -> new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.OAUTH2_AUTHENTICATION_NOT_FOUND_001)),
            oAuth2AuthorizedClient
        )
    );
  }

  @Override
  @Transactional
  public void linkOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal,
      UserJwt principalJwt
  ) {

    Optional<LinkedOAuth2Account> linkedOAuth2AccountOptional = linkedOAuth2AccountRepository.findByIdFetchUser(
        new LinkedOAuth2AccountId(
            oAuth2AuthorizedClient.getClientRegistration().getRegistrationId(),
            oAuth2AuthorizedClient.getPrincipalName()
        )
    );

    if (linkedOAuth2AccountOptional.isEmpty()) {
      User existingUser = userRepository.findByIdFetchLinkedOAuth2Accounts(Long.parseLong(principalJwt.getSub()))
          .orElseThrow(() -> new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_004));
      LinkedOAuth2Account linkedOAuth2Account = linkedOAuth2AccountMapper.toLinkedOAuth2Account(oAuth2AuthorizedClient);
      linkedOAuth2Account.setUser(existingUser);
      existingUser.getLinkedOAuth2Accounts().add(linkedOAuth2AccountRepository.save(linkedOAuth2Account));
      userRepository.save(existingUser);

    } else {
      LinkedOAuth2Account existingLinkedOAuth2Account = linkedOAuth2AccountOptional.get();
      if (existingLinkedOAuth2Account.getUser() == null) {
        throw new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_OAUTH2_AUTHENTICATION_005);
      } else if (!existingLinkedOAuth2Account.getUser().getId().equals(Long.parseLong(principalJwt.getSub()))) {
        throw new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_OAUTH2_AUTHENTICATION_001);
      } else {
        linkedOAuth2AccountRepository.save(
            linkedOAuth2AccountMapper.updateLinkedOAuth2Account(
                existingLinkedOAuth2Account,
                oAuth2AuthorizedClient
            )
        );
      }
    }

  }

  @Override
  @Transactional
  public void saveOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {

    Optional<LinkedOAuth2Account> linkedOAuth2AccountOptional = linkedOAuth2AccountRepository.findByIdFetchUser(
        new LinkedOAuth2AccountId(
            oAuth2AuthorizedClient.getClientRegistration().getRegistrationId(),
            oAuth2AuthorizedClient.getPrincipalName()
        )
    );

    if (linkedOAuth2AccountOptional.isEmpty()) {
      linkedOAuth2AccountRepository.save(linkedOAuth2AccountMapper.toLinkedOAuth2Account(oAuth2AuthorizedClient));

    } else {
      linkedOAuth2AccountRepository.save(
          linkedOAuth2AccountMapper.updateLinkedOAuth2Account(
              linkedOAuth2AccountOptional.get(),
              oAuth2AuthorizedClient
          )
      );
    }

  }

  @Override
  @Transactional
  public void removeOAuth2AuthorizedClient(
      String clientRegistrationId,
      String principalName
  ) {
    linkedOAuth2AccountRepository.deleteById(
        new LinkedOAuth2AccountId(
            clientRegistrationId,
            principalName
        )
    );
  }

  @Override
  @Transactional
  public void unlinkOAuth2AuthorizedClient(
      UserPrincipal principal,
      String clientRegistrationId
  ) {

    LinkedOAuth2Account linkedOAuth2Account = linkedOAuth2AccountRepository.findByUserIdAndClientRegistrationIdFetchUser(
        principal.getId(),
        clientRegistrationId
    ).orElseThrow(() -> new ResourceNotFoundException(AuthenticationErrorDictionary.OAUTH2_AUTHENTICATION_NOT_FOUND_001));

    if (linkedOAuth2Account.getAccessTokenValue() == null
        && linkedOAuth2Account.getRefreshTokenValue() == null
    ) {
      throw new ResourceNotFoundException(AuthenticationErrorDictionary.OAUTH2_AUTHENTICATION_NOT_FOUND_002);
    }

    User userAccount = linkedOAuth2Account.getUser();
    if (userAccount.getPassword() == null
        && userAccount.getLinkedOAuth2Accounts().size() == 1) {
      throw new ConflictException(AuthenticationErrorDictionary.OAUTH2_UNLINK_ONLY_AUTHENTICATION_METHOD_001);
    }

    // if (OAuth2AuthenticationConfigs.GITHUB_REGISTRATION_ID.equalsIgnoreCase(clientRegistrationId)) {
    //   revokeGithubAccessToken(linkedOAuth2Account);
    // } else if (OAuth2AuthenticationConfigs.GOOGLE_REGISTRATION_ID.equalsIgnoreCase(clientRegistrationId)) {
    //   revokeGoogleAccessToken(linkedOAuth2Account);
    // }

    userAccount.getLinkedOAuth2Accounts().remove(linkedOAuth2Account);
    userRepository.save(userAccount);
    linkedOAuth2AccountRepository.delete(linkedOAuth2Account);

  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> buildJwtTokens(
      String clientRegistrationId,
      String principalName
  ) {

    LinkedOAuth2Account linkedOAuth2Account = linkedOAuth2AccountRepository.findByIdFetchUser(
            new LinkedOAuth2AccountId(
                clientRegistrationId,
                principalName))
        .orElseThrow(() -> new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.OAUTH2_AUTHENTICATION_NOT_FOUND_001));

    if (linkedOAuth2Account.getUser() == null) {
      throw new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.OAUTH2_AUTHENTICATION_NOT_FOUND_003);
    }

    return buildJwtTokens(authenticationMapper.toUserPrincipal(linkedOAuth2Account.getUser()));

  }

  // TODO: add try catch for request error
  private void revokeGithubAccessToken(LinkedOAuth2Account linkedOAuth2Account) {

    ClientRegistration clientRegistration =
        clientRegistrationRepository.findByRegistrationId(OAuth2AuthenticationConfigs.GITHUB_REGISTRATION_ID);

    try {
      ResponseEntity<Void> response = restClient.method(HttpMethod.DELETE)
          .uri(String.format("https://api.github.com/applications/%s/grant", clientRegistration.getClientId()))
          .headers(httpHeaders -> {
            httpHeaders.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
            httpHeaders.add("Accept", "application/vnd.github+json");
            httpHeaders.add("X-GitHub-Api-Version", "2022-11-28");
          })
          .body(Map.of("token", linkedOAuth2Account.getAccessTokenValue()))
          .retrieve()
          .toBodilessEntity();

      if (!response.getStatusCode().is2xxSuccessful()) {
        log.error(
            "Error while revoking Github authorization for {}: {} - {}",
            linkedOAuth2Account.getId().getPrincipalName(),
            response.getStatusCode(),
            response.getBody()
        );
        throw new BadGatewayException(AuthenticationErrorDictionary.INVALID_OAUTH2_REVOCATION_REQUEST_001);
      }
    } catch (Exception exception) {
      log.error("Failed to revoke Github authorization for {}: {}",
          linkedOAuth2Account.getId().getPrincipalName(),
          exception.getMessage(),
          exception
      );
      throw new BadGatewayException(AuthenticationErrorDictionary.INVALID_OAUTH2_REVOCATION_REQUEST_001);
    }

  }

  // TODO: add try catch for request error
  private void revokeGoogleAccessToken(LinkedOAuth2Account linkedOAuth2Account) {

    try {
      ResponseEntity<Void> response = restClient.post()
          .uri(UriComponentsBuilder.fromUriString("https://oauth2.googleapis.com/revoke")
              .queryParam(
                  "token",
                  linkedOAuth2Account.getAccessTokenValue() == null
                      ? linkedOAuth2Account.getRefreshTokenValue()
                      : linkedOAuth2Account.getAccessTokenValue())
              .build()
              .toUri())
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(Map.of(
              "access_token",
              linkedOAuth2Account.getRefreshTokenValue() == null
                  ? linkedOAuth2Account.getAccessTokenValue()
                  : linkedOAuth2Account.getRefreshTokenValue()))
          .retrieve()
          .toBodilessEntity();

      if (!response.getStatusCode().is2xxSuccessful()) {
        log.error(
            "Error while revoking Google token for {}: {} - {}",
            linkedOAuth2Account.getId().getPrincipalName(),
            response.getStatusCode(),
            response.getBody()
        );
        throw new BadGatewayException(AuthenticationErrorDictionary.INVALID_OAUTH2_REVOCATION_REQUEST_001);
      }
    } catch (Exception exception) {
      log.error("Failed to revoke Google authorization for {}: {}",
          linkedOAuth2Account.getId().getPrincipalName(),
          exception.getMessage(),
          exception
      );
      throw new BadGatewayException(AuthenticationErrorDictionary.INVALID_OAUTH2_REVOCATION_REQUEST_001);
    }

  }

  private String generateUniqueUsername(String username) {

    String uniqueUsername = username;
    int counter = 1;

    while (userRepository.findByUsername(uniqueUsername).isPresent()) {
      uniqueUsername = username + counter++;
    }

    return uniqueUsername;

  }

  private String generateUniqueDisplayName(String username) {

    String displayName = username;
    int counter = 1;

    while (userRepository.findByDisplayName(displayName).isPresent()) {
      displayName = username + counter++;
    }

    return displayName;

  }

  private boolean isLocalEnvironment() {
    return environment.acceptsProfiles(Profiles.of("local"));
  }

  private ResponseCookie buildCookie(ResponseCookie.ResponseCookieBuilder builder) {

    builder.httpOnly(true)
        .sameSite("Lax")
        .secure(!isLocalEnvironment());
    if (!isLocalEnvironment()) {
      builder.domain("api.recipecatalogue.com");
    }

    return builder.build();

  }

}
