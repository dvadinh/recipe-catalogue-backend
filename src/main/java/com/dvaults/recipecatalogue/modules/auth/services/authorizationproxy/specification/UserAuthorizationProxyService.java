package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.data.util.Pair;

import java.util.List;

public interface UserAuthorizationProxyService {

  List<? extends UserResponse> findAll(boolean isSummaryResponse);

  UserDetailsResponse findById(long id);

  Pair<UserDetailsResponse, JwtDecision> updateById(
      UserPrincipal principal,
      long id,
      PatchUserRequest patchUserRequest
  );

  void deleteById(long id);

}
