package com.dvaults.recipecatalogue.modules.auth.services.api.implementation;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.mappers.LinkedOAuth2AccountMapper;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.LinkedOAuth2AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkedOAuth2AccountServiceImpl implements LinkedOAuth2AccountService {

  private final LinkedOAuth2AccountMapper linkedOAuth2AccountMapper;

  @Override
  public List<LinkedOAuth2AccountResponse> findAllByUser(User user) {
    return linkedOAuth2AccountMapper.toLinkedOAuth2AccountResponseList(user.getLinkedOAuth2Accounts());
  }

}
