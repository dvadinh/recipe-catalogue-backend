package com.dvaults.recipecatalogue.modules.core.dtos.section;

import com.dvaults.recipecatalogue.modules.core.dtos.section.requests.PutSectionRequest;

public record PutSection(

    String title,

    String description

) {

  public PutSection(PutSectionRequest putSectionRequest) {
    this(putSectionRequest.title(), putSectionRequest.description());
  }

}
