package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import java.util.List;

public interface UserAuthorizationProxyService {

  List<UserDetailsResponse> findAll();

  UserDetailsResponse findById(long id);

  UserDetailsResponse updateById(
      long id,
      PatchUserRequest patchUserRequest
  );

  void deleteById(long id);

}
