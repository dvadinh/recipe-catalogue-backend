package com.dvaults.recipecatalogue.security.authorization.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.access.AccessDeniedException;

@Getter
@Setter
@ToString
public class AuthorizationException extends AccessDeniedException {

  private ErrorDictionaryDescriptor errorDictionaryDescriptor;
  private Object details;

  public AuthorizationException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
  }

  public AuthorizationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
    setDetails(details);
  }

}
