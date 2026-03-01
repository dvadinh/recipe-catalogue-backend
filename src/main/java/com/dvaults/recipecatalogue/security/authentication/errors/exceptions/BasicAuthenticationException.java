package com.dvaults.recipecatalogue.security.authentication.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
@ToString
public class BasicAuthenticationException extends AuthenticationException {

  private ErrorDictionaryDescriptor errorDictionaryDescriptor;
  private Object details;

  public BasicAuthenticationException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
  }

  public BasicAuthenticationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
    setDetails(details);
  }

}