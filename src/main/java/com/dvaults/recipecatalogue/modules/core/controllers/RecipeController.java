package com.dvaults.recipecatalogue.modules.core.controllers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.DeleteRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeResponse;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.RecipeAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.ValidPostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.ValidPutRecipeRequest;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class RecipeController {

  private final RecipeAuthorizationProxyService recipeAuthorizationProxyService;

  @GetMapping(path = "/recipes")
  public Callable<ResponseEntity<List<RecipeResponse>>> getAll(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(recipeAuthorizationProxyService.findAll(principal));
  }

  @GetMapping(path = "/recipes/{recipeId}")
  public Callable<ResponseEntity<RecipeResponse>> getById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(recipeAuthorizationProxyService.findById(principal, recipeId));
  }

  @PostMapping(path = "/recipes")
  public Callable<ResponseEntity<RecipeResponse>> post(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody @ValidPostRecipeRequest PostRecipeRequest postRecipeRequest
  ) {
    return () -> ResponseEntity.status(HttpStatus.CREATED)
        .body(recipeAuthorizationProxyService.create(principal, postRecipeRequest));
  }

  @PutMapping(path = "/recipes/{recipeId}")
  public Callable<ResponseEntity<RecipeResponse>> putById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId,
      @RequestBody @ValidPutRecipeRequest PutRecipeRequest putRecipeRequest
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(recipeAuthorizationProxyService.updateById(principal, recipeId, putRecipeRequest));
  }

  @PutMapping(path = "/recipes/{recipeId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Callable<ResponseEntity<MediaResponse>> putMediaById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId,
      @RequestParam("media") MultipartFile media
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(recipeAuthorizationProxyService.updateMediaById(principal, recipeId, media));
  }

  @DeleteMapping(path = "/recipes/{recipeId}/media")
  public Callable<ResponseEntity<Void>> deleteMediaById(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId
  ) {
    return () -> {

      recipeAuthorizationProxyService.deleteMediaById(principal, recipeId);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

  @DeleteMapping(path = "/recipes")
  public Callable<ResponseEntity<Void>> deleteAll(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody DeleteRecipeRequest deleteRecipeRequest
  ) {
    return () -> {

      recipeAuthorizationProxyService.deleteAll(principal, deleteRecipeRequest);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    };
  }

}
