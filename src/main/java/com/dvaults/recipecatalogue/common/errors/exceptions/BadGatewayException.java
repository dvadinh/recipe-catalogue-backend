package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import org.springframework.http.HttpStatus;

public class BadGatewayException extends BaseException {

  public BadGatewayException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.BAD_GATEWAY);
  }

  public BadGatewayException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor, HttpStatus.BAD_GATEWAY, details);
  }

  public BadGatewayException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super(errorDictionaryDescriptor, httpStatus);
  }

  public BadGatewayException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
  }

}

