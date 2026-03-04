package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.annotation.Nullable;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseCookie;

public interface AuthenticationAuthorizationProxyService {

  UserDetailsResponse findUserByPrincipal(@Nullable UserPrincipal principal);

  UserDetailsResponse create(PostUserRequest postUserRequest);

  JwtDecision updateUsernamePassword(
      UserPrincipal principal,
      PatchUsernamePasswordRequest patchUsernamePasswordRequest
  );

  Pair<ResponseCookie, ResponseCookie> refreshJwtTokens(
      String jwtAccessToken,
      String jwtRefreshToken
  );

  void triggerJwtAccessTokenReset(String sub);

  void revokeJwtTokens(String sub);

  Pair<ResponseCookie, ResponseCookie> buildJwtRevokingTokens();

  void unlinkOAuth2AuthorizedClient(
      UserPrincipal principal,
      String clientRegistrationId
  );

}
