package com.dvaults.recipecatalogue.modules.auth.controllers;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.AuthenticationAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.UserAuthorizationProxyService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
  public Callable<ResponseEntity<List<UserDetailsResponse>>> getAll(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(userAuthorizationProxyService.findAll());
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
      @RequestBody @ValidPatchUserRequest PatchUserRequest patchUserRequest
  ) {
    return () -> {

      UserDetailsResponse userDetailsResponse = userAuthorizationProxyService.updateById(
          userId,
          patchUserRequest
      );

      if (principal.getId().equals(userId)) {
        Pair<ResponseCookie, ResponseCookie> jwtRevokingTokenCookies = authenticationAuthorizationProxyService.buildJwtRevokingTokens();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getFirst().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getSecond().toString());
      }

      return ResponseEntity.status(HttpStatus.OK)
          .body(userDetailsResponse);

    };
  }

  @DeleteMapping(path = "/users/{userId}")
  public Callable<ResponseEntity<Void>> deleteById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long userId
  ) {
    return () -> {

      userAuthorizationProxyService.deleteById(userId);
      if (principal.getId().equals(userId)) {
        Pair<ResponseCookie, ResponseCookie> jwtRevokingTokenCookies = authenticationAuthorizationProxyService.buildJwtRevokingTokens();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getFirst().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRevokingTokenCookies.getSecond().toString());
      }

      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .build();

    };
  }

}
