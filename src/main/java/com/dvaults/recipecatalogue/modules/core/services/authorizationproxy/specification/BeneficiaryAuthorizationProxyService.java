package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.PostBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import java.util.List;

public interface BeneficiaryAuthorizationProxyService {

  List<BeneficiaryResponse> findAllByRecipeId(
      UserPrincipal principal,
      long recipeId
  );

  List<BeneficiaryResponse> createByRecipeId(
      UserPrincipal principal,
      long recipeId,
      PostBeneficiaryRequest postBeneficiaryRequest
  );

  void deleteAllByRecipeId(
      UserPrincipal principal,
      long recipeId,
      DeleteBeneficiaryRequest deleteBeneficiaryRequest
  );

}
