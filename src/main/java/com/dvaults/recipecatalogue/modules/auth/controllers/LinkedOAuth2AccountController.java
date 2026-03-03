package com.dvaults.recipecatalogue.modules.auth.controllers;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.services.authorizationproxy.specification.LinkedOAuth2AccountAuthorizationService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@Validated
@RequiredArgsConstructor
public class LinkedOAuth2AccountController {

  private final LinkedOAuth2AccountAuthorizationService linkedOAuth2AccountAuthorizationService;

  @GetMapping(path = "/users/{userId}/oauth2-accounts")
  public Callable<ResponseEntity<List<LinkedOAuth2AccountResponse>>> getAllByUserId(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long userId
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(linkedOAuth2AccountAuthorizationService.findAllByUserId(userId));
  }

}
