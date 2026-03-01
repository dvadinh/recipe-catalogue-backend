package com.dvaults.recipecatalogue.modules.auth.services.api.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

public interface UserService {

  @PreAuthorize("@userAuthorizationService.preAuthorizeFindAllByUsers(principal)")
  List<UserResponse> findAllByUsers(List<User> users);

  @PreAuthorize("@userAuthorizationService.preAuthorizeFindByUser(principal, #user)")
  UserResponse findByUser(User user);

  @PreAuthorize("@userAuthorizationService.preAuthorizeDeleteByUser(principal, #user)")
  void deleteByUser(User user);

  @PreAuthorize("@userAuthorizationService.preAuthorizeUpdateByUser(principal, #user, #screenedRequest)")
  Pair<UserResponse, Boolean> updateByUser(
      User user,
      PatchUserRequest screenedRequest
  );

}
