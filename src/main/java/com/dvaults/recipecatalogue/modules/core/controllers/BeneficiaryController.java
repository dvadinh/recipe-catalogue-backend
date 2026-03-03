package com.dvaults.recipecatalogue.modules.core.controllers;

import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.PostBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.BeneficiaryAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.beneficiary.ValidDeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.beneficiary.ValidPostBeneficiaryRequest;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@Validated
@RequiredArgsConstructor
public class BeneficiaryController {

  private final BeneficiaryAuthorizationProxyService beneficiaryAuthorizationProxyService;

  @GetMapping(path = "/recipes/{recipeId}/beneficiaries")
  public Callable<ResponseEntity<List<BeneficiaryResponse>>> getAllByRecipeId(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId
  ) {
    return () -> ResponseEntity.status(HttpStatus.OK)
        .body(beneficiaryAuthorizationProxyService.findAllByRecipeId(principal, recipeId));
  }

  @PostMapping(path = "/recipes/{recipeId}/beneficiaries")
  public Callable<ResponseEntity<List<BeneficiaryResponse>>> postByRecipeId(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId,
      @RequestBody @ValidPostBeneficiaryRequest PostBeneficiaryRequest postBeneficiaryRequest
  ) {
    return () -> ResponseEntity.status(HttpStatus.CREATED)
        .body(beneficiaryAuthorizationProxyService.createByRecipeId(principal, recipeId, postBeneficiaryRequest));
  }

  @DeleteMapping(path = "/recipes/{recipeId}/beneficiaries")
  public Callable<ResponseEntity<Void>> deleteAllByRecipeId(
      HttpServletRequest request,
      HttpServletResponse response,
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable Long recipeId,
      @RequestBody @ValidDeleteBeneficiaryRequest DeleteBeneficiaryRequest deleteBeneficiaryRequest
  ) {
    return () -> {
      beneficiaryAuthorizationProxyService.deleteAllByRecipeId(principal, recipeId, deleteBeneficiaryRequest);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    };
  }

}
