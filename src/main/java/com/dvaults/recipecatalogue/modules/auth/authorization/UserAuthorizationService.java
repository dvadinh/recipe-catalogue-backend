package com.dvaults.recipecatalogue.modules.auth.authorization;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userAuthorizationService")
public class UserAuthorizationService {

  public boolean preAuthorizeFindAll(UserPrincipal principal) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_001);

  }

  public boolean preAuthorizeFindByUser(
      UserPrincipal principal,
      User user
  ) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    if (principal.getId().equals(user.getId())) return true;

    throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_002);

  }

  public boolean preAuthorizeUpdateByUser(
      UserPrincipal principal,
      User user,
      PatchUserRequest screenedRequest
  ) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    if (screenedRequest.typeOperation() != null) {
      throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_003);
    }

    if (screenedRequest.enabledOperation() != null) {
      throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_003);
    }

    if (screenedRequest.descriptionOperation() != null) {
      throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_003);
    }

    if (principal.getId().equals(user.getId())) return true;

    throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_005);

  }

  public boolean preAuthorizeDeleteAllByUsers(
      UserPrincipal principal,
      List<User> users
  ) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    if (users.isEmpty()
        || (users.size() == 1 && principal.getId().equals(users.getFirst().getId()))
    ) {
      return true;
    }

    throw new AuthorizationException(UserErrorDictionary.USER_ACCESS_DENIED_005);

  }

}
