package com.dvaults.recipecatalogue.common.dtos.responses;

public record MediaResponse(

    String presignedUrl,

    String name,

    String contentType

) {
}
