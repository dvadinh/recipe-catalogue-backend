package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class SavedRequestOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final RequestCache requestCache;
  private final AuthenticationService authenticationService;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws ServletException, IOException {

    Cookie jwtRequestCookie = WebUtils.getCookie(request, JwtConfigs.ACCESS_TOKEN_COOKIE_NAME);
    if (jwtRequestCookie == null) {

      OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
      Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = authenticationService.buildJwtTokens(
          oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
          oAuth2AuthenticationToken.getName()
      );

      response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getFirst().toString());
      response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getSecond().toString());

    }

    super.handle(request, response, authentication);

  }

  @Override
  public String determineTargetUrl(
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);
    String targetUri = SecurityUtils.getQueryParameterValue(savedRequest, OAuth2AuthenticationConfigs.TARGET_URL_QUERY_PARAMETER_NAME);

    requestCache.removeRequest(request, response);

    return UriComponentsBuilder.fromUriString(targetUri)
        .queryParam(
            OAuth2AuthenticationConfigs.STATUS_QUERY_PARAMETER_NAME,
            OAuth2AuthenticationConfigs.SUCCESS_STATUS_QUERY_PARAMETER_VALUE)
        .build()
        .toUriString();

  }

}
