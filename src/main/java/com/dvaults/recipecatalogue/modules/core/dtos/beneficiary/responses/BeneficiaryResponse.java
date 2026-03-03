package com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;

public record BeneficiaryResponse(

    RecipeSummaryResponse recipe,

    UserSummaryResponse user

) {
}
