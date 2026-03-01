package com.dvaults.recipecatalogue.modules.auth.authorization;

import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

@Service("authenticationAuthorizationService")
public class AuthenticationAuthorizationService {

  public boolean preAuthorizeCreate(UserPrincipal principal) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    throw new AuthorizationException(AuthenticationErrorDictionary.AUTHENTICATION_ACCESS_DENIED_001);

  }

  public boolean preAuthorizeUpdateUsernamePassword(
      UserPrincipal principal,
      User user
  ) {

    if (principal.getAuthority() == Authority.ADMIN) return true;

    if (principal.getId().equals(user.getId())) return true;

    throw new AuthorizationException(AuthenticationErrorDictionary.AUTHENTICATION_ACCESS_DENIED_002);

  }

}
