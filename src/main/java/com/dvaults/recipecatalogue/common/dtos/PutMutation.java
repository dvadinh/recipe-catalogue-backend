package com.dvaults.recipecatalogue.common.dtos;

public record PutMutation<RequestType, EntityType>(

    RequestType request,

    EntityType entity,

    Integer number

) {

  public boolean isNew() {
    return entity == null;
  }

}
