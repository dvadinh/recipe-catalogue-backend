package com.dvaults.recipecatalogue.modules.core.controllers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.StepAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;

@RestController
@Validated
@RequiredArgsConstructor
public class StepController {

  private final StepAuthorizationProxyService stepAuthorizationProxyService;

  @PutMapping(path = "/steps/{stepId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Callable<ResponseEntity<MediaResponse>> putMediaById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long stepId,
      @RequestParam("media") MultipartFile media
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(stepAuthorizationProxyService.updateMediaById(principal, stepId, media));
  }

  @DeleteMapping(path = "/steps/{stepId}/media")
  public Callable<ResponseEntity<Void>> deleteMediaById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long stepId
  ) {
    return () -> {

      stepAuthorizationProxyService.deleteMediaById(principal, stepId);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

}
