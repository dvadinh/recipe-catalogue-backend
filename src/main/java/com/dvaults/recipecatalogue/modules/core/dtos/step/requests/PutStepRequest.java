package com.dvaults.recipecatalogue.modules.core.dtos.step.requests;

public record PutStepRequest(

    Long id,

    String title,

    String description

) {
}
