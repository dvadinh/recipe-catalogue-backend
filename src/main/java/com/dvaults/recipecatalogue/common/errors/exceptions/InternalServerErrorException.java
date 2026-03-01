package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends BaseException {

  public InternalServerErrorException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor, HttpStatus.INTERNAL_SERVER_ERROR, details);
  }

  public InternalServerErrorException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super(errorDictionaryDescriptor, httpStatus);
  }

  public InternalServerErrorException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
  }

}
