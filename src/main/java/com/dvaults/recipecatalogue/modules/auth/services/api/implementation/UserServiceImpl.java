package com.dvaults.recipecatalogue.modules.auth.services.api.implementation;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.ConflictException;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public List<? extends UserResponse> findAllByUsers(
      List<User> users,
      boolean isSummaryResponse
  ) {
    if (isSummaryResponse) {
      return userMapper.toUserSummaryResponseList(users);
    } else {
     return userMapper.toUserDetailsResponseList(users);
    }
  }

  @Override
  public UserDetailsResponse findByUser(User user) {
    return userMapper.toUserDetailsResponse(user);
  }

  @Override
  @Transactional
  public List<String> deleteAllByUsers(List<User> users) {

    userRepository.deleteAll(users);

    return users.stream()
        .map(user -> String.valueOf(user.getId()))
        .toList();

  }

  @Override
  @Transactional
  public Pair<UserDetailsResponse, JwtDecision> updateByUser(
      User user,
      PatchUserRequest screenedRequest
  ) {

    boolean mutated = false;

    if (screenedRequest.displayNameOperation() == PatchRequestOperation.UPDATE) {
      if (!user.getDisplayName().equals(screenedRequest.displayName())) {
        userRepository.findByDisplayName(screenedRequest.displayName())
            .ifPresentOrElse(
                existingUser -> {
                  throw new ConflictException(UserErrorDictionary.USER_DISPLAY_NAME_ALREADY_EXISTS_001);
                },
                () -> user.setDisplayName(screenedRequest.displayName())
            );
        mutated = true;
      }
    }

    if (screenedRequest.typeOperation() == PatchRequestOperation.UPDATE) {
      user.setType(screenedRequest.type());
      mutated = true;
    }

    if (screenedRequest.enabledOperation() == PatchRequestOperation.UPDATE) {
      user.setEnabled(screenedRequest.enabled());
      mutated = true;
    }

    if (screenedRequest.descriptionOperation() != null) {
      if (screenedRequest.descriptionOperation() == PatchRequestOperation.UPDATE)
        user.setDescription(screenedRequest.description());
      else user.setDescription(null);
      mutated = true;
    }

    return Pair.of(
        userMapper.toUserDetailsResponse(
            mutated
                ? userRepository.save(user)
                : user),
        mutated ? JwtDecision.TRIGGER_RESET : JwtDecision.NONE
    );

  }

}
