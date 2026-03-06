package com.dvaults.recipecatalogue.modules.auth.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.UsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.annotation.Nullable;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.UUID;

public interface AuthenticationService {

  UserDetailsResponse findUserByPrincipal(@Nullable UserPrincipal principal);

  UserPrincipal toUserPrincipal(UserJwt principalJwt);

  void signUpByUsername(UsernamePasswordRequest usernamePasswordRequest);

  @PreAuthorize("@authenticationAuthorizationService.preAuthorizeCreate(principal)")
  UserDetailsResponse create(PostUserRequest screenedRequest);

  @PreAuthorize("@authenticationAuthorizationService.preAuthorizeUpdateUsernamePassword(principal, #user)")
  JwtDecision updateUsernamePassword(
      User user,
      PatchUsernamePasswordRequest screenedRequest
  );

  default Pair<ResponseCookie, ResponseCookie> buildJwtTokens(UserPrincipal principal) {
    return buildJwtTokens(principal, UUID.randomUUID().toString());
  }

  Pair<ResponseCookie, ResponseCookie> buildJwtTokens(
      UserPrincipal principal,
      String jti
  );

  Pair<ResponseCookie, ResponseCookie> buildJwtRevokingTokens();

  Pair<ResponseCookie, ResponseCookie> refreshJwtTokens(
      UserJwt principalJwt,
      String refreshToken
  );

  void triggerJwtAccessTokenReset(String sub);

  boolean isJwtAccessTokenForcedReset(String sub);

  boolean areValidJwtTokens(
      String sub,
      String jti
  );

  void revokeJwtTokens(String sub);

  UserJwt parseJwtAccessToken(String jwtAccessTokenString);

  OAuth2AuthorizedClient loadOAuth2AuthorizedClient(
      String clientRegistrationId,
      String principalName
  );

  void saveSigningUpOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      OAuth2AuthenticationToken oAuth2Principal
  );

  void saveSigningInOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      OAuth2AuthenticationToken oAuth2Principal
  );

  void linkOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      OAuth2AuthenticationToken oAuth2Principal,
      UserJwt principalJwt
  );

  void saveOAuth2AuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      OAuth2AuthenticationToken oAuth2Principal
  );

  void removeOAuth2AuthorizedClient(
      String clientRegistrationId,
      String principalName
  );

  void unlinkOAuth2AuthorizedClient(
      UserPrincipal principal,
      String clientRegistrationId
  );

  Pair<ResponseCookie, ResponseCookie> buildJwtTokens(
      String clientRegistrationId,
      String principalName
  );

}
