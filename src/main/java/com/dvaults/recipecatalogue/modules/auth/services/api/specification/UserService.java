package com.dvaults.recipecatalogue.modules.auth.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

public interface UserService {

  @PreAuthorize("@userAuthorizationService.preAuthorizeFindAll(principal)")
  List<? extends UserResponse> findAll(boolean isSummaryResponse);

  @PreAuthorize("@userAuthorizationService.preAuthorizeFindByUser(principal, #user)")
  UserDetailsResponse findByUser(User user);

  @PreAuthorize("@userAuthorizationService.preAuthorizeUpdateByUser(principal, #user, #screenedRequest)")
  Pair<UserDetailsResponse, JwtDecision> updateByUser(
      User user,
      PatchUserRequest screenedRequest
  );

  @PreAuthorize("@userAuthorizationService.preAuthorizeDeleteAllByUsers(principal, #users)")
  List<String> deleteAllByUsers(List<User> users);

}
