package com.dvaults.recipecatalogue.modules.core.dtos.recipe;

import com.dvaults.recipecatalogue.common.dtos.PutMutation;
import com.dvaults.recipecatalogue.modules.core.dtos.section.PutSectionContext;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import java.util.List;

public record PutRecipeContext(

    PutMutation<PutRecipe, Recipe> recipeMutation,

    List<PutSectionContext> sectionContexts

) {
}
