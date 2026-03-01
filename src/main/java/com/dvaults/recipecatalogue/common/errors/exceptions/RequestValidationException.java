package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import org.springframework.http.HttpStatus;

public class RequestValidationException extends BaseException {

  public RequestValidationException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.BAD_REQUEST);
  }

  public RequestValidationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor, HttpStatus.BAD_REQUEST, details);
  }

  public RequestValidationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super(errorDictionaryDescriptor, httpStatus);
  }

  public RequestValidationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
  }

}
