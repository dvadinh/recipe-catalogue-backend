package com.dvaults.recipecatalogue.modules.auth.validation.validators;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.DeleteUserRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidDeleteUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteUserRequestValidator implements ConstraintValidator<ValidDeleteUserRequest, DeleteUserRequest> {

  private final UserMapper userMapper;

  @Override
  public boolean isValid(
      DeleteUserRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    DeleteUserRequest screenedRequest = userMapper.screenDeleteUserRequest(request);

    if (screenedRequest.userIds() == null
        || screenedRequest.userIds().isEmpty()
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "userIds",
          UserErrorDictionary.USER_IDS_ERROR_MESSAGE_001
      );
    } else {
      if (screenedRequest.userIds()
          .stream()
          .anyMatch(userId -> userId == null || userId <= 0)
      ) {
        isValid = false;
        ValidationUtils.constructErrorFromMessage(
            errors,
            "userIds",
            UserErrorDictionary.USER_IDS_ERROR_MESSAGE_002
        );
      }
    }

    if (!isValid) {
      throw new RequestValidationException(
          UserErrorDictionary.INVALID_USER_DETAILS_003,
          errors
      );
    }

    return isValid;

  }

}
