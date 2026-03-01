package com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests;

import com.dvaults.recipecatalogue.modules.core.dtos.section.requests.PutSectionRequest;
import java.util.List;

public record PutRecipeRequest(

    String name,

    String description,

    List<PutSectionRequest> sections

) {
}
