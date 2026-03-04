package com.dvaults.recipecatalogue.modules.auth.dtos.user.responses;

public record UserSummaryResponse(

    Long id,

    String displayName

) implements UserResponse {
}
