package com.dvaults.recipecatalogue.security.authentication.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
@ToString
public class RestAuthenticationException extends AuthenticationException {

  private ErrorDictionaryDescriptor errorDictionaryDescriptor;
  private Object details;

  public RestAuthenticationException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
  }

  public RestAuthenticationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
    setDetails(details);
  }

}
