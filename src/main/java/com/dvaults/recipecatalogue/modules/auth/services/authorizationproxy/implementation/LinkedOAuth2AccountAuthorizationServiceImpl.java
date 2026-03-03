package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.LinkedOAuth2AccountService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.LinkedOAuth2AccountAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkedOAuth2AccountAuthorizationServiceImpl implements LinkedOAuth2AccountAuthorizationService {

  private final LinkedOAuth2AccountService linkedOAuth2AccountService;

  private final UserRepository userRepository;

  @Override
  public List<LinkedOAuth2AccountResponse> findAllByUserId(long userId) {
    return linkedOAuth2AccountService.findAllByUser(
        userRepository.findByIdFetchLinkedOAuth2Accounts(userId)
            .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001))
    );
  }

}
