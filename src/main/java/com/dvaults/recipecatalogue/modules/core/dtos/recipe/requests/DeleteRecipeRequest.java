package com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests;

import java.util.List;

public record DeleteRecipeRequest(

    List<Long> recipeIds

) {
}
