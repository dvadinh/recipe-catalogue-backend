package com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses;

import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;

public record RecipeSummaryResponse(

    Long id,

    String name,

    RecipeAccessLevel accessLevel

) {
}
