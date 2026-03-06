package com.dvaults.recipecatalogue.modules.auth.dtos.user.requests;

import java.util.List;

public record DeleteUserRequest(

    List<Long> userIds

) {
}
