package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import java.util.List;

public interface UserAuthenticationProxyService {

  List<UserResponse> findAll();

  UserResponse findById(long id);

  UserResponse updateById(
      long id,
      PatchUserRequest patchUserRequest
  );

  void deleteById(long id);

}
