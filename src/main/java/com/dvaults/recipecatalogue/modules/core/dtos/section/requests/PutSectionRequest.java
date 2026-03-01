package com.dvaults.recipecatalogue.modules.core.dtos.section.requests;

import com.dvaults.recipecatalogue.modules.core.dtos.step.requests.PutStepRequest;
import java.util.List;

public record PutSectionRequest(

    Long id,

    String title,

    String description,

    List<PutStepRequest> steps

) {
}
