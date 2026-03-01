package com.dvaults.recipecatalogue.modules.core.dtos.step;

import com.dvaults.recipecatalogue.modules.core.dtos.step.requests.PutStepRequest;

public record PutStep(

    String title,

    String description

) {

  public PutStep(PutStepRequest putStepRequest) {
    this(putStepRequest.title(), putStepRequest.description());
  }

}
