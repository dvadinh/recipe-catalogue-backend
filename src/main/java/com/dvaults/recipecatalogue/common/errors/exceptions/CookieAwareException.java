package com.dvaults.recipecatalogue.common.errors.exceptions;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class CookieAwareException extends BaseException {

  List<ResponseCookie> cookies;

  public CookieAwareException(ErrorDictionaryDescriptor errorDictionaryDescriptor) {
    super(errorDictionaryDescriptor, HttpStatus.UNAUTHORIZED);
  }

  public CookieAwareException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      List<ResponseCookie> cookies
  ) {
    super(errorDictionaryDescriptor, errorDictionaryDescriptor.getStatus());
    setCookies(cookies);
  }

  public CookieAwareException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      List<ResponseCookie> cookies,
      Object details
  ) {
    super(errorDictionaryDescriptor, HttpStatus.UNAUTHORIZED, details);
    setCookies(cookies);
  }

  public CookieAwareException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      List<ResponseCookie> cookies
  ) {
    super(errorDictionaryDescriptor, httpStatus);
    setCookies(cookies);
  }

  public CookieAwareException(
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      HttpStatus httpStatus,
      List<ResponseCookie> cookies,
      Object details
  ) {
    super(errorDictionaryDescriptor, httpStatus, details);
    setCookies(cookies);
  }

}
