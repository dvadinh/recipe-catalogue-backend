package com.dvaults.recipecatalogue.modules.auth.dtos.user.requests;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;

public record PatchUsernamePasswordRequest(

    Long id,

    String username,

    PatchRequestOperation usernameOperation,

    String password,

    PatchRequestOperation passwordOperation

) {
}
