package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;

import java.util.List;

public interface BeneficiaryService {

  List<BeneficiaryResponse> findAllByRecipe(Recipe recipe);

  List<BeneficiaryResponse> createByRecipeAndUsers(
      Recipe recipe,
      List<User> users
  );

  void deleteAllByRecipe(
      Recipe recipe,
      DeleteBeneficiaryRequest screenedRequest
  );

}
