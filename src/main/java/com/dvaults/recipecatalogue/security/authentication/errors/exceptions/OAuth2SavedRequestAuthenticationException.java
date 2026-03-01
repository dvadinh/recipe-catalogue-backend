package com.dvaults.recipecatalogue.security.authentication.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
@ToString
public class OAuth2SavedRequestAuthenticationException extends AuthenticationException {

  private ErrorDictionaryDescriptor errorDictionaryDescriptor;
  private String targetUrl;

  public OAuth2SavedRequestAuthenticationException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
  }

  public OAuth2SavedRequestAuthenticationException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      String targetUrl
  ) {
    super(errorDictionaryDescriptor.getErrorId());
    setErrorDictionaryDescriptor(errorDictionaryDescriptor);
    setTargetUrl(targetUrl);
  }

}
