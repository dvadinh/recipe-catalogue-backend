package com.dvaults.recipecatalogue.modules.auth.dtos.user.requests;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;

public record PostUserRequest(

    String username,

    String password,

    String displayName,

    String description,

    Authority type,

    Boolean enabled

) {
}
