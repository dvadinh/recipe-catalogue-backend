package com.dvaults.recipecatalogue.modules.auth.dtos.user.requests;

public record UsernamePasswordRequest(

    String username,

    String password

) {
}
