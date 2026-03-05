package com.dvaults.recipecatalogue.modules.auth.authorization;

import com.dvaults.recipecatalogue.modules.auth.errors.LinkedOAuth2AccountErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

@Service("linkedOAuth2AccountAuthorizationService")
public class LinkedOAuth2AccountAuthorizationService {

  public boolean preAuthorizeFindAllByUserId(
      UserPrincipal principal,
      User user
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(user.getId())) {
      return true;
    }

    throw new AuthorizationException(LinkedOAuth2AccountErrorDictionary.LINKED_OAUTH2_ACCOUNT_ACCESS_DENIED);

  }

}
