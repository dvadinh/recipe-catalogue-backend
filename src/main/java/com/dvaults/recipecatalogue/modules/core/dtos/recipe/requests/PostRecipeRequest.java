package com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests;

import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;

public record PostRecipeRequest(

    String name,

    String description,

    RecipeAccessLevel accessLevel

) {
}
