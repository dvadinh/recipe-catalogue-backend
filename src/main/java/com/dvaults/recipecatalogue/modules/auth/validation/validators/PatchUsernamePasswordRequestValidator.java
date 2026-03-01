package com.dvaults.recipecatalogue.modules.auth.validation.validators;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPatchUsernamePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PatchUsernamePasswordRequestValidator implements ConstraintValidator<ValidPatchUsernamePasswordRequest, PatchUsernamePasswordRequest> {

  private final UserMapper userMapper;

  @Override
  public boolean isValid(
      PatchUsernamePasswordRequest request,
      ConstraintValidatorContext context
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PatchUsernamePasswordRequest screenedRequest = userMapper.screenPatchUsernamePasswordRequest(request);

    if (screenedRequest.id() == null) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "id",
          AuthenticationErrorDictionary.ID_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.usernameOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "usernameOperation",
          AuthenticationErrorDictionary.USERNAME_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.usernameOperation() == PatchRequestOperation.UPDATE
        && !StringUtils.hasText(screenedRequest.username())
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "username",
          AuthenticationErrorDictionary.USERNAME_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.passwordOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "passwordOperation",
          AuthenticationErrorDictionary.PASSWORD_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.passwordOperation() == PatchRequestOperation.UPDATE
        && !StringUtils.hasText(screenedRequest.password())
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "password",
          AuthenticationErrorDictionary.PASSWORD_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_004,
          errors
      );
    }

    return isValid;

  }

}
