package com.dvaults.recipecatalogue.security.authentication.basic;

import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Setter
public class PostBasicAuthenticationFilter extends BasicAuthenticationFilter {

  private AuthenticationService authenticationService;

  public PostBasicAuthenticationFilter(
      AuthenticationManager authenticationManager,
      AuthenticationEntryPoint authenticationEntryPoint,
      AuthenticationConverter authenticationConverter,
      AuthenticationService authenticationService
  ) {
    super(authenticationManager, authenticationEntryPoint);
    setAuthenticationConverter(authenticationConverter);
    setAuthenticationService(authenticationService);
  }

  @Override
  protected void onSuccessfulAuthentication(
      @NonNull HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {

    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

    Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = authenticationService.buildJwtTokens(principal);

    response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getFirst().toString());
    response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getSecond().toString());

  }

}
