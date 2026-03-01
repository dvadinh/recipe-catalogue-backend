package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.NOT_FOUND);
  }

  public ResourceNotFoundException(
      ErrorDictionaryDescriptor exceptionDictionary,
      Object details
  ) {
    super(exceptionDictionary, HttpStatus.NOT_FOUND, details);
  }

  public ResourceNotFoundException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super(errorDictionaryDescriptor, httpStatus);
  }

  public ResourceNotFoundException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
  }

}
