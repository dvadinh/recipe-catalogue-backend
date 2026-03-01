package com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses;

public record BeneficiaryNonAdminResponse(

    Long recipeId,

    Long userId

) implements BeneficiaryResponse {
}
