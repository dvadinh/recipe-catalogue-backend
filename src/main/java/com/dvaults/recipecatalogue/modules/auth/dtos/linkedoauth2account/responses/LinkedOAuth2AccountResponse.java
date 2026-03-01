package com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses;

import java.util.Set;

public record LinkedOAuth2AccountResponse(

    String provider,

    String accessTokenType,

    Set<String> accessTokenScopes

) {
}
