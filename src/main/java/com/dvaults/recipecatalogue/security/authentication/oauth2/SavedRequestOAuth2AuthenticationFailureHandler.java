package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.OAuth2SavedRequestAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class SavedRequestOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final RequestCache requestCache;
  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException {

    String targetUri;
    String statusQueryParameterValue = OAuth2AuthenticationConfigs.FAILURE_STATUS_QUERY_PARAMETER_VALUE;
    Object errorMessage;

    if (exception instanceof OAuth2SavedRequestAuthenticationException oAuth2SavedRequestAuthenticationException) {

      if (request.getRequestURI().startsWith(OAuth2AuthenticationConfigs.REDIRECT_URI)) {

        DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);

        targetUri = Objects.requireNonNullElse(
            oAuth2SavedRequestAuthenticationException.getTargetUrl(),
            SecurityUtils.getQueryParameterValue(
                savedRequest,
                OAuth2AuthenticationConfigs.TARGET_URL_QUERY_PARAMETER_NAME
            ));
        errorMessage = oAuth2SavedRequestAuthenticationException.getErrorDictionaryDescriptor()
            .getDetails();

      } else {
        SecurityUtils.writeErrorResponse(response, oAuth2SavedRequestAuthenticationException.getErrorDictionaryDescriptor());
        return;
      }

    } else {

      DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);

      targetUri = SecurityUtils.getQueryParameterValue(
          savedRequest,
          OAuth2AuthenticationConfigs.TARGET_URL_QUERY_PARAMETER_NAME);
      errorMessage = exception.getCause() instanceof OAuth2SavedRequestAuthenticationException oAuth2SavedRequestAuthenticationException
          ? oAuth2SavedRequestAuthenticationException.getErrorDictionaryDescriptor().getDetails()
          : exception.getMessage();

    }

    String uri = UriComponentsBuilder.fromUriString(targetUri)
        .queryParam(
            OAuth2AuthenticationConfigs.STATUS_QUERY_PARAMETER_NAME,
            statusQueryParameterValue)
        .queryParam(
            OAuth2AuthenticationConfigs.ERROR_QUERY_PARAMETER_NAME,
            errorMessage)
        .build()
        .toUriString();

    requestCache.removeRequest(request, response);
    redirectStrategy.sendRedirect(request, response, uri);

  }

}
