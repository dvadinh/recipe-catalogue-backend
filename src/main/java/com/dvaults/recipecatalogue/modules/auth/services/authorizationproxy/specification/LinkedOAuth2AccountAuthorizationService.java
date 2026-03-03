package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import java.util.List;

public interface LinkedOAuth2AccountAuthorizationService {

  List<LinkedOAuth2AccountResponse> findAllByUserId(long userId);

}
