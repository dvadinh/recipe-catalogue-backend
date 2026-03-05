package com.dvaults.recipecatalogue.modules.auth.services.api.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface LinkedOAuth2AccountService {

  @PreAuthorize("@linkedOAuth2AccountAuthorizationService.preAuthorizeFindAllByUserId(principal, #user)")
  List<LinkedOAuth2AccountResponse> findAllByUser(User user);

}
