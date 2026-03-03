package com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserDetailsResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.section.responses.SectionResponse;
import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;

import java.time.Instant;
import java.util.List;

public record RecipeDetailsResponse(

    Long id,

    String name,

    RecipeAccessLevel accessLevel,

    String description,

    Instant lastUpdatedAt,

    UserSummaryResponse owner,

    MediaResponse media,

    List<SectionResponse> sections

) {
}
