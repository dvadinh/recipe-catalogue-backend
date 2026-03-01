package com.dvaults.recipecatalogue.modules.auth.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthenticationErrorDictionary implements ErrorDictionaryDescriptor {

  USERNAME_NOT_FOUND_001(HttpStatus.NOT_FOUND, "Username not found."),

  BASIC_AUTHENTICATION_NOT_FOUND_001(HttpStatus.UNAUTHORIZED, "Username and password authentication not found or not valid."),

  INVALID_BASIC_AUTHENTICATION_001(HttpStatus.UNAUTHORIZED, "Incorrect username and password."),
  INVALID_BASIC_AUTHENTICATION_002(HttpStatus.CONFLICT, "Username already exists."),
  INVALID_BASIC_AUTHENTICATION_003(HttpStatus.BAD_REQUEST, "Invalid username and/or password."),
  INVALID_BASIC_AUTHENTICATION_004(HttpStatus.BAD_REQUEST, "Invalid username and/or password update."),
  INVALID_BASIC_AUTHENTICATION_005(HttpStatus.UNAUTHORIZED, "Username cannot be empty or not provided."),
  INVALID_BASIC_AUTHENTICATION_006(
      HttpStatus.UNAUTHORIZED,
      "Password cannot be empty or not provided. If you signed up with an OAuth2 account, sign in with the OAuth2 option"
  ),

  OAUTH2_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth2 provider not found."),

  OAUTH2_AUTHENTICATION_NOT_FOUND_001(HttpStatus.UNAUTHORIZED, "OAuth2 account not found. Please sign up or link it first."),
  OAUTH2_AUTHENTICATION_NOT_FOUND_002(HttpStatus.UNAUTHORIZED, "OAuth2 access token and refresh token not found. Please re-authenticate with your OAuth2 account."),
  OAUTH2_AUTHENTICATION_NOT_FOUND_003(HttpStatus.UNAUTHORIZED, "Account not signed up. Please sign up first."),

  INVALID_OAUTH2_AUTHENTICATION_001(HttpStatus.CONFLICT, "OAuth2 account already linked to a user."),
  INVALID_OAUTH2_AUTHENTICATION_002(HttpStatus.CONFLICT, "OAuth2 account already exists."),
  INVALID_OAUTH2_AUTHENTICATION_003(HttpStatus.BAD_REQUEST, "target_url query parameter not found."),
  INVALID_OAUTH2_AUTHENTICATION_004(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot extract the redirect url from the OAuth2 request."),
  INVALID_OAUTH2_AUTHENTICATION_005(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth2 account already exists, but not linked to any users."),

  OAUTH2_UNLINK_ONLY_AUTHENTICATION_METHOD_001(
      HttpStatus.CONFLICT,
      "Cannot delete OAuth2 account as it is the only authentication method. If you want to delete your account, call the account deletion endpoint."
  ),

  JWT_ACCESS_TOKEN_NOT_FOUND_001(HttpStatus.UNAUTHORIZED, "JWT access token not found."),

  INVALID_JWT_ACCESS_TOKEN_001(HttpStatus.UNAUTHORIZED, "Invalid JWT access token."),
  INVALID_JWT_ACCESS_TOKEN_002(HttpStatus.UNAUTHORIZED, "JWT access token revoked. Please sign in again."),
  INVALID_JWT_ACCESS_TOKEN_003(HttpStatus.UNAUTHORIZED, "JWT access token outdated, but no JWT refresh token available. Please sign in again."),
  INVALID_JWT_ACCESS_TOKEN_004(HttpStatus.UNAUTHORIZED, "No users found by the JWT access token. Please sign out and try again later."),
  INVALID_JWT_ACCESS_TOKEN_005(HttpStatus.UNAUTHORIZED, "JWT access token expired. Please refresh it."),

  JWT_REFRESH_TOKEN_NOT_FOUND_001(HttpStatus.UNAUTHORIZED, "JWT refresh token not found. Cannot refresh the JWT access token. Please sign in again."),

  INVALID_JWT_REFRESH_TOKEN_001(HttpStatus.CONFLICT, "Invalid JWT refresh token. Please sign in again."),

  USER_NOT_ENABLED_001(HttpStatus.UNAUTHORIZED, "User is enabled."),
  USERNAME_ALREADY_EXISTS_001(HttpStatus.CONFLICT, "Username already exists."),

  AUTHENTICATION_ACCESS_DENIED_001(HttpStatus.FORBIDDEN, "Only admins create new users."),
  AUTHENTICATION_ACCESS_DENIED_002(HttpStatus.FORBIDDEN, "Only admins and the owner can update credentials information."),

  UNEXPECTED_AUTHENTICATION_ERROR_001(HttpStatus.INTERNAL_SERVER_ERROR, "Only basic authentication sign-up/in requests are handled here.")

  ;

  public static final String ID_ERROR_MESSAGE_001 = "User id must be provided.";

  public static final String USERNAME_ERROR_MESSAGE_001 = "Username must not be empty when updating.";

  public static final String PASSWORD_ERROR_MESSAGE_001 = "Password must not be empty when updating.";

  public static final String USERNAME_OPERATION_ERROR_MESSAGE_001 = "Username cannot be cleared.";

  public static final String PASSWORD_OPERATION_ERROR_MESSAGE_001 = "Password cannot be cleared.";

  public static final String OAUTH2_CLIENT_REGISTRATION_ID_ERROR_MESSAGE_001 =
      "Client registration id must be one of the following: " + OAuth2AuthenticationConfigs.ALL_REGISTRATION_IDS;

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return this.name();
  }

}
