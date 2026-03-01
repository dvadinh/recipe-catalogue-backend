package com.dvaults.recipecatalogue.common.errors.exceptions.base;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public abstract class BaseException extends RuntimeException {

  protected int status;

  protected final String errorId;

  protected final Object details;

  public BaseException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super();
    this.status = errorDictionaryDescriptor.getStatus().value();
    this.errorId = errorDictionaryDescriptor.getErrorId();
    this.details = errorDictionaryDescriptor.getDetails();
  }

  public BaseException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus
  ) {
    super();
    this.status = httpStatus.value();
    this.errorId = errorDictionaryDescriptor.getErrorId();
    this.details = errorDictionaryDescriptor.getDetails();
  }

  public BaseException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super();
    this.status = errorDictionaryDescriptor.getStatus().value();
    this.errorId = errorDictionaryDescriptor.getErrorId();
    this.details = details;
  }

  public BaseException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      Object details
  ) {
    super();
    this.status = httpStatus.value();
    this.errorId = errorDictionaryDescriptor.getErrorId();
    this.details = details;
  }

}
