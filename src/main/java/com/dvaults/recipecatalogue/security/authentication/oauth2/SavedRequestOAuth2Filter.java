package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.common.utils.SecurityUtils;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.OAuth2SavedRequestAuthenticationException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import java.io.IOException;

@RequiredArgsConstructor
public class SavedRequestOAuth2Filter extends OncePerRequestFilter {

  private final AuthenticationService authenticationService;
  private final RequestCache requestCache;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String requestUri = request.getRequestURI();

    if (requestUri.startsWith(OAuth2AuthenticationConfigs.LINK_AUTHORIZATION_REQUEST_URI)) {

      Cookie jwtAccessCookie = WebUtils.getCookie(request, JwtConfigs.ACCESS_TOKEN_COOKIE_NAME);

      if (jwtAccessCookie == null
          || !StringUtils.hasText(jwtAccessCookie.getValue())
      ) {
        authenticationEntryPoint.commence(
            request,
            response,
            new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.JWT_ACCESS_TOKEN_NOT_FOUND_001)
        );
        return;
      }

      try {
        authenticationService.parseJwtAccessToken(jwtAccessCookie.getValue());
      } catch (JwtException jwtException) {
        authenticationEntryPoint.commence(
            request,
            response,
            new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_001)
        );
        return;
      }

    }

    if (!requestUri.startsWith(OAuth2AuthenticationConfigs.REDIRECT_URI)) {
      String target = SecurityUtils.getQueryParameterValue(
          request,
          OAuth2AuthenticationConfigs.TARGET_URL_QUERY_PARAMETER_NAME
      );
      if (!StringUtils.hasText(target) || !UrlUtils.isAbsoluteUrl(target)) {
        authenticationEntryPoint.commence(
            request,
            response,
            new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_OAUTH2_AUTHENTICATION_003)
        );
        return;
      }

      requestCache.saveRequest(request, response);

    }

    filterChain.doFilter(request, response);

  }

}
