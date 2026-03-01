package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.annotation.Nullable;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseCookie;

public interface AuthenticationAuthorizationProxyService {

  UserResponse findUserByPrincipal(@Nullable UserPrincipal principal);

  UserResponse create(PostUserRequest postUserRequest);

  void updateUsernamePassword(PatchUsernamePasswordRequest patchUsernamePasswordRequest);

  Pair<ResponseCookie, ResponseCookie> refreshJwtTokens(
      String jwtAccessToken,
      String jwtRefreshToken
  );

  Pair<ResponseCookie, ResponseCookie> buildJwtRevokingTokens();

  void unlinkOAuth2AuthorizedClient(
      UserPrincipal principal,
      String clientRegistrationId
  );

}
