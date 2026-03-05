package com.dvaults.recipecatalogue.modules.core.authorization;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.core.errors.BeneficiaryErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

@Service("beneficiaryAuthorizationService")
public class BeneficiaryAuthorizationService {

  public boolean preAuthorizeFindAllByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(BeneficiaryErrorDictionary.BENEFICIARY_ACCESS_DENIED_001);

  }

  public boolean preAuthorizeCreateByRecipeAndUsers(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(BeneficiaryErrorDictionary.BENEFICIARY_ACCESS_DENIED_002);

  }

  public boolean preAuthorizeDeleteAllByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(BeneficiaryErrorDictionary.BENEFICIARY_ACCESS_DENIED_003);

  }

}
