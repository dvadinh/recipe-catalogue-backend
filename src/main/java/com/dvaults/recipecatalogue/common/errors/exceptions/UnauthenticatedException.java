package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends BaseException {

  public UnauthenticatedException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.UNAUTHORIZED);
  }

  public UnauthenticatedException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor, HttpStatus.UNAUTHORIZED, details);
  }

  public UnauthenticatedException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super(errorDictionaryDescriptor, httpStatus);
  }

  public UnauthenticatedException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
  }

}
