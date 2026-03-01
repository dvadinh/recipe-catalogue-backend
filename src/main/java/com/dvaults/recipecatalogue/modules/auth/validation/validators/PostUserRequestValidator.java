package com.dvaults.recipecatalogue.modules.auth.validation.validators;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPostUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PostUserRequestValidator implements ConstraintValidator<ValidPostUserRequest, PostUserRequest> {

  private final UserMapper userMapper;

  @Override
  public boolean isValid(
      PostUserRequest request,
      ConstraintValidatorContext context
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PostUserRequest screenedRequest = userMapper.screenPostUserRequest(request);

    if (!StringUtils.hasText(screenedRequest.username())) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "username",
          UserErrorDictionary.USERNAME_ERROR_MESSAGE_001
      );
    }

    if (!StringUtils.hasText(screenedRequest.password())) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "password",
          UserErrorDictionary.PASSWORD_ERROR_MESSAGE_001
      );
    }

    if (!StringUtils.hasText(screenedRequest.displayName())) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "displayName",
          UserErrorDictionary.DISPLAY_NAME_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.description() != null
        && !StringUtils.hasText(screenedRequest.description())
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "description",
          UserErrorDictionary.DESCRIPTION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.type() == null) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "type",
          UserErrorDictionary.TYPE_ERROR_MESSAGE_002
      );
    }

    if (screenedRequest.enabled() == null) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "enabled",
          UserErrorDictionary.ENABLED_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          UserErrorDictionary.INVALID_USER_DETAILS_001,
          errors
      );
    }

    return isValid;

  }

}
