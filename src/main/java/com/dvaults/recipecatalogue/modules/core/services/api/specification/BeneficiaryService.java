package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface BeneficiaryService {

  @PreAuthorize("@beneficiaryAuthorizationService.preAuthorizeFindAllByRecipe(principal, #recipe)")
  List<BeneficiaryResponse> findAllByRecipe(Recipe recipe);

  @PreAuthorize("@beneficiaryAuthorizationService.preAuthorizeCreateByRecipeAndUsers(principal, #recipe)")
  List<BeneficiaryResponse> createByRecipeAndUsers(
      Recipe recipe,
      List<User> users
  );

  @PreAuthorize("@beneficiaryAuthorizationService.preAuthorizeDeleteAllByRecipe(principal, #recipe)")
  void deleteAllByRecipe(
      Recipe recipe,
      DeleteBeneficiaryRequest screenedRequest
  );

}
