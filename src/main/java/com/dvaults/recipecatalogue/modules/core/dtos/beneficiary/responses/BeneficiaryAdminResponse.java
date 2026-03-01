package com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;

public record BeneficiaryAdminResponse(

    Long recipeId,

    UserResponse user

) implements BeneficiaryResponse {
}
