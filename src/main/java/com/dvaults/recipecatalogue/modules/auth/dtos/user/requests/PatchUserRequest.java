package com.dvaults.recipecatalogue.modules.auth.dtos.user.requests;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.modules.auth.models.Authority;

public record PatchUserRequest(

    String displayName,

    PatchRequestOperation displayNameOperation,

    String description,

    PatchRequestOperation descriptionOperation,

    Authority type,

    PatchRequestOperation typeOperation,

    Boolean enabled,

    PatchRequestOperation enabledOperation

) {
}
