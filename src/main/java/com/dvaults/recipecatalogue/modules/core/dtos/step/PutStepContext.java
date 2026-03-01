package com.dvaults.recipecatalogue.modules.core.dtos.step;

import com.dvaults.recipecatalogue.common.dtos.PutMutation;
import com.dvaults.recipecatalogue.modules.core.models.Step;

public record PutStepContext(

    PutMutation<PutStep, Step> stepMutation

) {
}
