package com.dvaults.recipecatalogue.modules.auth.controllers;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.configs.BasicAuthenticationConfigs;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.SecurityConfigs;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.AuthenticationAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidClientRegistrationId;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPostUserRequest;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationAuthorizationProxyService authenticationAuthorizationProxyService;

  @GetMapping(path = SecurityConfigs.WHO_AM_I_URI)
  public Callable<ResponseEntity<UserDetailsResponse>> whoAmI(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(authenticationAuthorizationProxyService.findUserByPrincipal(principal));
  }

  @PostMapping(path = JwtConfigs.REFRESH_TOKEN_URI)
  public Callable<ResponseEntity<Void>> refresh(
      HttpServletRequest request,
      HttpServletResponse response,
      @CookieValue(name = JwtConfigs.ACCESS_TOKEN_COOKIE_NAME, required = false) String jwtAccessToken,
      @CookieValue(name = JwtConfigs.REFRESH_TOKEN_COOKIE_NAME, required = false) String jwtRefreshToken
  ) {
    return () -> {

      Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = authenticationAuthorizationProxyService.refreshJwtTokens(
          jwtAccessToken,
          jwtRefreshToken
      );
      response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getFirst().toString());
      response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getSecond().toString());

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

  @PostMapping(path = SecurityConfigs.SIGN_OUT_URI)
  public Callable<ResponseEntity<Void>> signOut(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    return () -> {

      Pair<ResponseCookie, ResponseCookie> jwtRevokingTokenCookies = authenticationAuthorizationProxyService.buildJwtRevokingTokens();
      response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getFirst().toString());
      response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getSecond().toString());

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

  @PostMapping(path = {
      BasicAuthenticationConfigs.SIGN_UP_URI,
      BasicAuthenticationConfigs.SIGN_IN_URI
  })
  public Callable<ResponseEntity<UserDetailsResponse>> basicAuth(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(authenticationAuthorizationProxyService.findUserByPrincipal(principal));
  }

  @PostMapping(path = "/users")
  public Callable<ResponseEntity<UserDetailsResponse>> post(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody @ValidPostUserRequest PostUserRequest postUserRequest
  ) {
    return () -> ResponseEntity.status(HttpStatus.CREATED)
        .body(authenticationAuthorizationProxyService.create(postUserRequest));
  }

  @PatchMapping(path = "/auth/basic/credentials")
  public Callable<ResponseEntity<Void>> patchCredentials(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody @ValidPatchUsernamePasswordRequest PatchUsernamePasswordRequest patchUsernamePasswordRequest,
      @CookieValue(name = JwtConfigs.ACCESS_TOKEN_COOKIE_NAME, required = false) String jwtAccessToken,
      @CookieValue(name = JwtConfigs.REFRESH_TOKEN_COOKIE_NAME, required = false) String jwtRefreshToken
  ) {
    return () -> {

      JwtDecision jwtDecision = authenticationAuthorizationProxyService.updateUsernamePassword(
          principal,
          patchUsernamePasswordRequest
      );

      if (jwtDecision == JwtDecision.RESET) {
        Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = authenticationAuthorizationProxyService.refreshJwtTokens(
            jwtAccessToken,
            jwtRefreshToken
        );
        response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getFirst().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getSecond().toString());

      } else if (jwtDecision == JwtDecision.TRIGGER_RESET) {
        authenticationAuthorizationProxyService.triggerJwtAccessTokenReset(String.valueOf(principal.getId()));

      } else if (jwtDecision == JwtDecision.TRIGGER_FORCE_REAUTHENTICATION) {
        authenticationAuthorizationProxyService.revokeJwtTokens(String.valueOf(principal.getId()));
      }

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

  @DeleteMapping(path = "/auth/oauth2/unlink/{clientRegistrationId}")
  public Callable<ResponseEntity<Void>> unlinkOAuth2Account(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable @ValidClientRegistrationId String clientRegistrationId
  ) {
    return () -> {

      authenticationAuthorizationProxyService.unlinkOAuth2AuthorizedClient(
          principal,
          clientRegistrationId
      );

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

}
