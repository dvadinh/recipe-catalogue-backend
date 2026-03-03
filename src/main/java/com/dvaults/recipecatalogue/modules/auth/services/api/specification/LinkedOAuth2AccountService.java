package com.dvaults.recipecatalogue.modules.auth.services.api.specification;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import java.util.List;

public interface LinkedOAuth2AccountService {

  List<LinkedOAuth2AccountResponse> findAllByUser(User user);

}
