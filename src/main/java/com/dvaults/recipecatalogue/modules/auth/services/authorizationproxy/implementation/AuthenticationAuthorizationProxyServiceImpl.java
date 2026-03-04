package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.AuthenticationMapper;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.AuthenticationAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.RestAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationAuthorizationProxyServiceImpl implements AuthenticationAuthorizationProxyService {

  private final AuthenticationService authenticationService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;
  private final AuthenticationMapper authenticationMapper;

  @Override
  public UserDetailsResponse findUserByPrincipal(@Nullable UserPrincipal principal) {
    return authenticationService.findUserByPrincipal(principal);
  }

  @Override
  public UserDetailsResponse create(PostUserRequest postUserRequest) {
    return authenticationService.create(userMapper.screenPostUserRequest(postUserRequest));
  }

  @Override
  public JwtDecision updateUsernamePassword(
      UserPrincipal principal,
      PatchUsernamePasswordRequest patchUsernamePasswordRequest
  ) {

    PatchUsernamePasswordRequest screenedRequest =
        userMapper.screenPatchUsernamePasswordRequest(patchUsernamePasswordRequest);

    JwtDecision jwtDecision = authenticationService.updateUsernamePassword(
        userRepository.findById(screenedRequest.id())
            .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001)),
        screenedRequest
    );

    if (jwtDecision == JwtDecision.NONE) {
      return JwtDecision.NONE;
    }

    if (!principal.getId().equals(screenedRequest.id())) {
      if (jwtDecision == JwtDecision.TRIGGER_RESET) {
        authenticationService.triggerJwtAccessTokenReset(String.valueOf(screenedRequest.id()));
        return JwtDecision.NONE;
      } else {
        return jwtDecision;
      }

    } else {
      return JwtDecision.RESET;
    }

  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> refreshJwtTokens(
      String jwtAccessToken,
      String jwtRefreshToken
  ) {

    UserJwt principalJwt;
    try {
      principalJwt = authenticationService.parseJwtAccessToken(jwtAccessToken);
    } catch (ExpiredJwtException expiredJwtException) {
      principalJwt = authenticationMapper.mapToUserJwt(expiredJwtException.getClaims());
      return authenticationService.refreshJwtTokens(principalJwt, jwtRefreshToken);
    } catch (JwtException jwtException) {
      throw new RestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_001);
    }

    return authenticationService.refreshJwtTokens(principalJwt, jwtRefreshToken);

  }

  @Override
  public void triggerJwtAccessTokenReset(String sub) {
    authenticationService.triggerJwtAccessTokenReset(sub);
  }

  @Override
  public void revokeJwtTokens(String sub) {
    authenticationService.revokeJwtTokens(sub);
  }

  @Override
  public Pair<ResponseCookie, ResponseCookie> buildJwtRevokingTokens() {
    return authenticationService.buildJwtRevokingTokens();
  }

  @Override
  public void unlinkOAuth2AuthorizedClient(
      UserPrincipal principal,
      String clientRegistrationId
  ) {
    authenticationService.unlinkOAuth2AuthorizedClient(principal, clientRegistrationId);
  }

}
