package com.dvaults.recipecatalogue.modules.auth.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum LinkedOAuth2AccountErrorDictionary implements ErrorDictionaryDescriptor {

  OAUTH2_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth2 provider not found."),
  OAUTH2_USER_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth2 user account not found."),
  OAUTH2_ACCOUNT_ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth2 account access token not found."),
  INVALID_OAUTH2_USER_ACCOUNT_PROVIDER_CONFIGURATION(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot retrieve OAuth2 provider end session endpoint."),
  INVALID_OAUTH2_USER_ACCOUNT_REVOCATION_REQUEST(HttpStatus.BAD_GATEWAY, "Cannot revoke OAuth2 access token from the provider."),
  OAUTH2_ACCOUNT_DELETION_BLOCKED(
      HttpStatus.CONFLICT,
      "Cannot delete OAuth2 account as it is the only authentication method. If you want to delete your account, call the account deletion endpoint."
  ),
  ;

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
