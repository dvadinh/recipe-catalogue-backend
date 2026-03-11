package com.dvaults.recipecatalogue.security.authentication.rest;

import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.SecurityConfigs;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.RestAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.errors.handlers.JsonAuthenticationEntryPoint;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

  private final SecurityContextRepository securityContextRepository;
  private final AuthenticationService authenticationService;

  private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
  private final AuthenticationEntryPoint authenticationEntryPoint = new JsonAuthenticationEntryPoint();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String requestUri = request.getRequestURI();
    if (SecurityConfigs.SIGN_OUT_URI.equals(requestUri)) {
      filterChain.doFilter(request, response);
      return;
    }

    Cookie jwtAccessCookie = WebUtils.getCookie(request, JwtConfigs.ACCESS_TOKEN_COOKIE_NAME);
    if (jwtAccessCookie == null
        || !StringUtils.hasText(jwtAccessCookie.getValue())
    ) {
      authenticationEntryPoint.commence(
          request,
          response,
          new RestAuthenticationException(AuthenticationErrorDictionary.JWT_ACCESS_TOKEN_NOT_FOUND_001)
      );
      return;
    }

    if (JwtConfigs.REFRESH_TOKEN_URI.equals(requestUri)) {
      Cookie jwtRefreshCookie = WebUtils.getCookie(request, JwtConfigs.REFRESH_TOKEN_COOKIE_NAME);
      if (jwtRefreshCookie == null
          || !StringUtils.hasText(jwtRefreshCookie.getValue())
      ) {
        Pair<ResponseCookie, ResponseCookie> jwtRevokingTokenCookies = authenticationService.buildJwtRevokingTokens();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getFirst().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getSecond().toString());
        authenticationEntryPoint.commence(
            request,
            response,
            new RestAuthenticationException(AuthenticationErrorDictionary.JWT_REFRESH_TOKEN_NOT_FOUND_001)
        );
        return;
      }
      filterChain.doFilter(request, response);
      return;
    }

    UserJwt userJwt;
    try {
      userJwt = authenticationService.parseJwtAccessToken(jwtAccessCookie.getValue());
    } catch (ExpiredJwtException expiredJwtException) {
      authenticationEntryPoint.commence(
          request,
          response,
          new RestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_005)
      );
      return;
    } catch (JwtException jwtException) {
      authenticationEntryPoint.commence(
          request,
          response,
          new RestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_001)
      );
      return;
    }

    if (!authenticationService.areValidJwtTokens(userJwt.getSub(), userJwt.getJti())) {
      authenticationEntryPoint.commence(
          request,
          response,
          new RestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_002)
      );
      return;
    }

    if (authenticationService.isJwtAccessTokenForcedReset(userJwt.getSub())) {
      Cookie jwtRefreshCookie = WebUtils.getCookie(request, JwtConfigs.REFRESH_TOKEN_COOKIE_NAME);
      if (jwtRefreshCookie == null || !StringUtils.hasText(jwtRefreshCookie.getValue())) {
        authenticationEntryPoint.commence(
            request,
            response,
            new RestAuthenticationException(AuthenticationErrorDictionary.INVALID_JWT_ACCESS_TOKEN_003)
        );
        return;
      }
      Pair<ResponseCookie, ResponseCookie> refreshedJwtTokens = authenticationService.refreshJwtTokens(userJwt, jwtRefreshCookie.getValue());
      userJwt = authenticationService.parseJwtAccessToken(refreshedJwtTokens.getFirst().getValue());
      response.addHeader(HttpHeaders.SET_COOKIE, refreshedJwtTokens.getFirst().toString());
      response.addHeader(HttpHeaders.SET_COOKIE, refreshedJwtTokens.getSecond().toString());
    }

    if (!userJwt.getEnabled()) {
      authenticationEntryPoint.commence(
          request,
          response,
          new RestAuthenticationException(AuthenticationErrorDictionary.USER_NOT_ENABLED_001)
      );
      return;
    }

    SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
    securityContext.setAuthentication(
        new UserAuthenticationToken(
            authenticationService.toUserPrincipal(userJwt),
            null,
            null
        )
    );
    securityContextHolderStrategy.setContext(securityContext);
    securityContextRepository.saveContext(securityContext, request, response);

    filterChain.doFilter(request, response);

  }

}
