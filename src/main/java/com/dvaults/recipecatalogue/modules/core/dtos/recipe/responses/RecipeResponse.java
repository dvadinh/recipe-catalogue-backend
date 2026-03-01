package com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.section.responses.SectionResponse;
import java.time.Instant;
import java.util.List;

public record RecipeResponse(

    Long id,

    String name,

    String description,

    Instant lastUpdatedAt,

    UserResponse owner,

    MediaResponse media,

    List<SectionResponse> sections

) {
}
