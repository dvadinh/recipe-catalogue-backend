package com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;

public record PatchRecipeAccessLevelRequest(

    RecipeAccessLevel accessLevel,

    PatchRequestOperation accessLevelOperation

) {
}
