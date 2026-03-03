package com.dvaults.recipecatalogue.modules.auth.validation.validators;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPatchUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PatchUserRequestValidator implements ConstraintValidator<ValidPatchUserRequest, PatchUserRequest> {

  private final UserMapper userMapper;

  @Override
  public boolean isValid(
      PatchUserRequest request,
      ConstraintValidatorContext context
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PatchUserRequest screenedRequest = userMapper.screenPatchUserRequest(request);

    if (screenedRequest.displayNameOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "displayNameOperation",
          UserErrorDictionary.DISPLAY_NAME_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.displayNameOperation() == PatchRequestOperation.UPDATE
        && !StringUtils.hasText(screenedRequest.displayName())
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "displayName",
          UserErrorDictionary.DISPLAY_NAME_ERROR_MESSAGE_002
      );
    }

    if (screenedRequest.descriptionOperation() == PatchRequestOperation.UPDATE
        && !StringUtils.hasText(screenedRequest.description())
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "description",
          UserErrorDictionary.DESCRIPTION_ERROR_MESSAGE_002
      );
    }

    if (screenedRequest.typeOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "typeOperation",
          UserErrorDictionary.TYPE_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.typeOperation() == PatchRequestOperation.UPDATE
        && screenedRequest.type() == null
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "type",
          UserErrorDictionary.TYPE_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.enabledOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "enabledOperation",
          UserErrorDictionary.ENABLED_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.enabledOperation() == PatchRequestOperation.UPDATE
        && screenedRequest.enabled() == null
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "enabled",
          UserErrorDictionary.ENABLED_ERROR_MESSAGE_002
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          UserErrorDictionary.INVALID_USER_DETAILS_002,
          errors
      );
    }

    return isValid;

  }

}
