package com.dvaults.recipecatalogue.modules.core.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum BeneficiaryErrorDictionary implements ErrorDictionaryDescriptor {

  BENEFICIARY_NOT_FOUND_001(HttpStatus.NOT_FOUND, "Beneficiary not found."),

  INVALID_BENEFICIARY_DETAILS_001(HttpStatus.BAD_REQUEST, "Invalid beneficiary creation details."),
  INVALID_BENEFICIARY_DETAILS_002(HttpStatus.BAD_REQUEST, "Invalid beneficiary deletion details."),
  INVALID_BENEFICIARY_DETAILS_003(HttpStatus.BAD_REQUEST, "Cannot add beneficiaries to a public recipe."),
  INVALID_BENEFICIARY_DETAILS_004(HttpStatus.BAD_REQUEST, "Cannot delete beneficiaries from a public recipe."),

  BENEFICIARY_ACCESS_DENIED_001(HttpStatus.FORBIDDEN, "Only admins can access this resource."),
  BENEFICIARY_ACCESS_DENIED_002(HttpStatus.FORBIDDEN, "Only admins and the owner can access this resource."),

  ;

  public static final String USER_IDS_ERROR_MESSAGE_001 = "User ids must be positive integers.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
