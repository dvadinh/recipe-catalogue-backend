package com.dvaults.recipecatalogue.modules.core.dtos.step.responses;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;

public record StepResponse(

    Long id,

    String title,

    Integer number,

    String description,

    MediaResponse media

) {
}
