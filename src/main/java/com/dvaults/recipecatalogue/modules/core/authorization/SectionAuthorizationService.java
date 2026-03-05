package com.dvaults.recipecatalogue.modules.core.authorization;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.core.errors.SectionErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

@Service("sectionAuthorizationService")
public class SectionAuthorizationService {

  public boolean preAuthorizeUpdateMediaBySection(
      UserPrincipal principal,
      Section section
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(section.getRecipe().getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(SectionErrorDictionary.SECTION_ACCESS_DENIED_001);

  }

  public boolean preAuthorizeDeleteMediaBySection(
      UserPrincipal principal,
      Section section
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(section.getRecipe().getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(SectionErrorDictionary.SECTION_ACCESS_DENIED_001);

  }

}
