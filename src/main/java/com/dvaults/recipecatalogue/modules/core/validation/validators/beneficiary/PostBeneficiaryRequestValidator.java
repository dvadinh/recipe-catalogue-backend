package com.dvaults.recipecatalogue.modules.core.validation.validators.beneficiary;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.PostBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.errors.BeneficiaryErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.BeneficiaryMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.beneficiary.ValidPostBeneficiaryRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PostBeneficiaryRequestValidator implements ConstraintValidator<ValidPostBeneficiaryRequest, PostBeneficiaryRequest> {

  private final BeneficiaryMapper beneficiaryMapper;

  @Override
  public boolean isValid(
      PostBeneficiaryRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PostBeneficiaryRequest screenedRequest = beneficiaryMapper.screenPostBeneficiaryRequest(request);

    if (screenedRequest.userIds() == null
        || screenedRequest.userIds().isEmpty()
        || screenedRequest.userIds()
            .stream()
            .anyMatch(userId -> userId == null || userId <= 0)
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "userIds",
          BeneficiaryErrorDictionary.USER_IDS_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          BeneficiaryErrorDictionary.INVALID_BENEFICIARY_DETAILS_001,
          errors
      );
    }

    return isValid;

  }

}