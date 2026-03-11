package com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.DeleteUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.UserService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.UserAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorizationProxyServiceImpl implements UserAuthorizationProxyService {

  private final UserService userService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public List<? extends UserResponse> findAll(boolean isSummaryResponse) {
    return userService.findAll(isSummaryResponse);
  }

  @Override
  public UserDetailsResponse findById(long id) {
    return userRepository.findById(id)
        .map(userService::findByUser)
        .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001));
  }

  @Override
  public Pair<UserDetailsResponse, JwtDecision> updateById(
      UserPrincipal principal,
      long id,
      PatchUserRequest patchUserRequest
  ) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(UserErrorDictionary.USER_NOT_FOUND_001));

    Pair<UserDetailsResponse, JwtDecision> userDetailsResponsePair = userService.updateByUser(
        user,
        userMapper.screenPatchUserRequest(patchUserRequest)
    );

    JwtDecision jwtDecision = userDetailsResponsePair.getSecond();
    if (jwtDecision == JwtDecision.TRIGGER_RESET
        && principal.getId().equals(id)
    ) {
      jwtDecision = JwtDecision.RESET;
    }

    return Pair.of(
        userDetailsResponsePair.getFirst(),
        jwtDecision
    );

  }

  @Override
  public List<String> deleteAll(DeleteUserRequest deleteUserRequest) {
    return userService.deleteAllByUsers(
        userRepository.findAllByIdIn(
            userMapper.screenDeleteUserRequest(deleteUserRequest).userIds()
        )
    );
  }

}
