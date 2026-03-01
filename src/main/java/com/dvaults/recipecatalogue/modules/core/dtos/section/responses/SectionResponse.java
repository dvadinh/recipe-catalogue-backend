package com.dvaults.recipecatalogue.modules.core.dtos.section.responses;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.step.responses.StepResponse;

import java.util.List;

public record SectionResponse(

    Long id,

    String title,

    Integer number,

    String description,

    MediaResponse media,

    List<StepResponse> steps

) {
}
