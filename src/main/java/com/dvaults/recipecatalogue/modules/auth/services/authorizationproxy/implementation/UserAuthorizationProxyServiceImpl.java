package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.UserService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.UserAuthorizationProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorizationProxyServiceImpl implements UserAuthorizationProxyService {

  private final UserService userService;
  private final AuthenticationService authenticationService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public List<UserDetailsResponse> findAll() {
    return userService.findAllByUsers(userRepository.findAll());
  }

  @Override
  public UserDetailsResponse findById(long id) {
    return userRepository.findById(id)
        .map(userService::findByUser)
        .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001));
  }

  @Override
  public UserDetailsResponse updateById(
      long id,
      PatchUserRequest patchUserRequest
  ) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001));

    PatchUserRequest screenedRequest = userMapper.screenPatchUserRequest(patchUserRequest);

    Pair<UserDetailsResponse, Boolean> responsePair = userService.updateByUser(
        user,
        screenedRequest
    );

    if (responsePair.getSecond()) {
      if (screenedRequest.typeOperation() == PatchRequestOperation.UPDATE
          || user.isEnabled() != responsePair.getFirst().enabled()
      ) {
        authenticationService.revokeJwtTokens(String.valueOf(user.getId()));
      } else {
        authenticationService.triggerJwtAccessTokenReset(String.valueOf(user.getId()));
      }
    }

    return responsePair.getFirst();

  }

  @Override
  public void deleteById(long id) {
    userRepository.findById(id)
        .ifPresentOrElse(
            user -> {
              authenticationService.revokeJwtTokens(String.valueOf(user.getId()));
              userService.deleteByUser(user);
            },
            () -> {
              throw new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001);
            });
  }

}
