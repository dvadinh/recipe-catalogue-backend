package com.dvaults.recipecatalogue.modules.auth.dtos.user.responses;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;

public record UserResponse(

    Long id,

    String username,

    String displayName,

    String description,

    Authority type,

    Boolean enabled

) {
}
