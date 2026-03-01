package com.dvaults.recipecatalogue.security.authorization.errors.handlers;

import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException exception
  ) throws IOException {

    if (exception instanceof AuthorizationException authorizationException) {
      SecurityUtils.writeErrorResponse(
          response,
          authorizationException.getErrorDictionaryDescriptor(),
          authorizationException.getDetails()
      );

    } else {
      SecurityUtils.writeAuthorizationErrorResponse(response, exception);
    }

  }

}
