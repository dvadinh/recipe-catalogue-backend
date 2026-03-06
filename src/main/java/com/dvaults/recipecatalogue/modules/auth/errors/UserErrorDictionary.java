package com.dvaults.recipecatalogue.modules.auth.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorDictionary implements ErrorDictionaryDescriptor {

  USER_NOT_FOUND_001(HttpStatus.NOT_FOUND, "User not found."),
  USER_NOT_FOUND_002(HttpStatus.INTERNAL_SERVER_ERROR, "User not found."),

  INVALID_USER_DETAILS_001(HttpStatus.BAD_REQUEST, "Invalid user creation details."),
  INVALID_USER_DETAILS_002(HttpStatus.BAD_REQUEST, "Invalid user update details."),
  INVALID_USER_DETAILS_003(HttpStatus.BAD_REQUEST, "Invalid user deletion details."),
  INVALID_USERNAME_PASSWORD_DETAILS_001(HttpStatus.BAD_REQUEST, "Invalid username and/or password."),

  USER_ACCESS_DENIED_001(HttpStatus.FORBIDDEN, "Only admins can access this resource."),
  USER_ACCESS_DENIED_002(HttpStatus.FORBIDDEN, "Only admins and the owner can access this resource."),
  USER_ACCESS_DENIED_003(HttpStatus.FORBIDDEN, "Only admins can disable users."),
  USER_ACCESS_DENIED_004(HttpStatus.FORBIDDEN, "Only admins can update user's description."),
  USER_ACCESS_DENIED_005(HttpStatus.FORBIDDEN, "Only admins can delete all users."),
  USER_ACCESS_DENIED_006(HttpStatus.FORBIDDEN, "Users can only delete themselves."),

  USER_DISPLAY_NAME_ALREADY_EXISTS_001(HttpStatus.CONFLICT, "Display name already exists."),

  ;

  public static final String USER_IDS_ERROR_MESSAGE_001 = "User ids must not be empty.";
  public static final String USER_IDS_ERROR_MESSAGE_002 = "User ids must be positive integers.";

  public static final String USERNAME_ERROR_MESSAGE_001 = "Username must be provided and not empty.";

  public static final String PASSWORD_ERROR_MESSAGE_001 = "Password must be provided and not empty.";

  public static final String DISPLAY_NAME_ERROR_MESSAGE_001 = "Display name must be provided and not empty.";
  public static final String DISPLAY_NAME_ERROR_MESSAGE_002 = "Display name must not be empty when updating.";

  public static final String DISPLAY_NAME_OPERATION_ERROR_MESSAGE_001 = "Display name cannot be cleared.";

  public static final String DESCRIPTION_ERROR_MESSAGE_001 = "Description must not be empty if provided.";
  public static final String DESCRIPTION_ERROR_MESSAGE_002 = "Description must not be empty when updating.";

  public static final String TYPE_OPERATION_ERROR_MESSAGE_001 = "User type cannot be cleared.";

  public static final String TYPE_ERROR_MESSAGE_001 = "User type must not be empty when updating.";
  public static final String TYPE_ERROR_MESSAGE_002 = "User type must not be empty.";

  public static final String ENABLED_OPERATION_ERROR_MESSAGE_001 = "Enabled status cannot be cleared.";

  public static final String ENABLED_ERROR_MESSAGE_001 = "Enabled status must not be empty if provided.";
  public static final String ENABLED_ERROR_MESSAGE_002 = "Enabled status must not be empty when updating.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
