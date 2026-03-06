package com.dvaults.recipecatalogue.modules.auth.controllers;

import com.dvaults.recipecatalogue.common.dtos.JwtDecision;
import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.DeleteUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.AuthenticationAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.UserAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidDeleteUserRequest;
import com.dvaults.recipecatalogue.modules.auth.validation.constraints.ValidPatchUserRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

  private final UserAuthorizationProxyService userAuthorizationProxyService;
  private final AuthenticationAuthorizationProxyService authenticationAuthorizationProxyService;

  @GetMapping(path = "/users")
  public Callable<ResponseEntity<List<? extends UserResponse>>> getAll(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestParam(name = "summary", required = false, defaultValue = "false") boolean isSummaryResponse
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(userAuthorizationProxyService.findAll(isSummaryResponse));
  }

  @GetMapping(path = "/users/{userId}")
  public Callable<ResponseEntity<UserDetailsResponse>> getById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long userId
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(userAuthorizationProxyService.findById(userId));
  }

  @PatchMapping(path = "/users/{userId}")
  public Callable<ResponseEntity<UserDetailsResponse>> patchById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long userId,
      @RequestBody @ValidPatchUserRequest PatchUserRequest patchUserRequest,
      @CookieValue(name = JwtConfigs.ACCESS_TOKEN_COOKIE_NAME, required = false) String jwtAccessToken,
      @CookieValue(name = JwtConfigs.REFRESH_TOKEN_COOKIE_NAME, required = false) String jwtRefreshToken
  ) {
    return () -> {

      Pair<UserDetailsResponse, JwtDecision> userDetailsResponsePair = userAuthorizationProxyService.updateById(
          principal,
          userId,
          patchUserRequest
      );

      if (userDetailsResponsePair.getSecond() == JwtDecision.RESET) {
        Pair<ResponseCookie, ResponseCookie> jwtTokenCookies = authenticationAuthorizationProxyService.refreshJwtTokens(
            jwtAccessToken,
            jwtRefreshToken
        );
        response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getFirst().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtTokenCookies.getSecond().toString());

      } else if (userDetailsResponsePair.getSecond() == JwtDecision.TRIGGER_RESET) {
        authenticationAuthorizationProxyService.triggerJwtAccessTokenReset(String.valueOf(userDetailsResponsePair.getFirst().id()));
      }

      return ResponseEntity.status(HttpStatus.OK)
          .body(userDetailsResponsePair.getFirst());

    };
  }

  @DeleteMapping(path = "/users")
  public Callable<ResponseEntity<Void>> deleteAll(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody @ValidDeleteUserRequest DeleteUserRequest deleteUserRequest
  ) {
    return () -> {

      userAuthorizationProxyService.deleteAll(deleteUserRequest)
          .forEach(sub -> {
            if (sub.equals(String.valueOf(principal.getId()))) {
              Pair<ResponseCookie, ResponseCookie> jwtRevokingTokenCookies = authenticationAuthorizationProxyService.buildJwtRevokingTokens();
              response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getFirst().toString());
              response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getSecond().toString());
            }
            authenticationAuthorizationProxyService.revokeJwtTokens(sub);
          });

      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .build();

    };
  }

}
