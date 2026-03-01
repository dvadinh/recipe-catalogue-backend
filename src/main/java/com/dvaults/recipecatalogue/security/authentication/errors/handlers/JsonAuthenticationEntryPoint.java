package com.dvaults.recipecatalogue.security.authentication.errors.handlers;

import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.OAuth2SavedRequestAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.RestAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException {

    switch (exception) {

      case BasicAuthenticationException basicAuthenticationException ->
          SecurityUtils.writeErrorResponse(
              response,
              basicAuthenticationException.getErrorDictionaryDescriptor(),
              basicAuthenticationException.getDetails()
          );

      case UsernameNotFoundException usernameNotFoundException ->
          SecurityUtils.writeErrorResponse(
              response,
              ((BasicAuthenticationException) usernameNotFoundException.getCause()).getErrorDictionaryDescriptor()
          );

      case OAuth2SavedRequestAuthenticationException oAuth2SavedRequestAuthenticationException ->
          SecurityUtils.writeErrorResponse(
              response,
              oAuth2SavedRequestAuthenticationException.getErrorDictionaryDescriptor()
          );

      case RestAuthenticationException restAuthenticationException -> SecurityUtils.writeErrorResponse(
          response,
          restAuthenticationException.getErrorDictionaryDescriptor()
      );

      default -> SecurityUtils.writeAuthenticationErrorResponse(response, exception);

    }

  }

}
