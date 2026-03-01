package com.dvaults.recipecatalogue.modules.auth.validation.validators;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidClientRegistrationId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.Map;

public class ClientRegistrationIdValidator implements ConstraintValidator<ValidClientRegistrationId, String> {

  @Override
  public boolean isValid(
      String value,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();

    if (OAuth2AuthenticationConfigs.ALL_REGISTRATION_IDS.stream()
        .noneMatch(registrationId -> registrationId.equalsIgnoreCase(value))
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "clientRegistrationId",
          AuthenticationErrorDictionary.OAUTH2_CLIENT_REGISTRATION_ID_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          AuthenticationErrorDictionary.OAUTH2_PROVIDER_NOT_FOUND,
          errors
      );
    }

    return isValid;

  }

}
