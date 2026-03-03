package com.dvaults.recipecatalogue.modules.core.validation.validators.beneficiary;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.errors.BeneficiaryErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.BeneficiaryMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.beneficiary.ValidDeleteBeneficiaryRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteBeneficiaryRequestValidator implements ConstraintValidator<ValidDeleteBeneficiaryRequest, DeleteBeneficiaryRequest> {

  private final BeneficiaryMapper beneficiaryMapper;

  @Override
  public boolean isValid(
      DeleteBeneficiaryRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    DeleteBeneficiaryRequest screenedRequest = beneficiaryMapper.screenDeleteBeneficiaryRequest(request);

    if (screenedRequest.userIds() != null
        && screenedRequest.userIds()
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
          BeneficiaryErrorDictionary.INVALID_BENEFICIARY_DETAILS_002,
          errors
      );
    }

    return isValid;

  }

}