package com.dvaults.recipecatalogue.modules.core.dtos.recipe;

import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;

public record PutRecipe(

    String name,

    String description

) {

  public PutRecipe(PutRecipeRequest putRecipeRequest) {
    this(putRecipeRequest.name(), putRecipeRequest.description());
  }

}
