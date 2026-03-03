package com.dvaults.recipecatalogue.modules.core.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum RecipeErrorDictionary implements ErrorDictionaryDescriptor {

  RECIPE_NOT_FOUND_001(HttpStatus.NOT_FOUND, "Recipe not found."),

  INVALID_RECIPE_DETAILS_001(HttpStatus.BAD_REQUEST, "Invalid recipe creation details."),
  INVALID_RECIPE_DETAILS_002(HttpStatus.BAD_REQUEST, "Invalid recipe update details."),
  INVALID_RECIPE_DETAILS_003(HttpStatus.BAD_REQUEST, "Invalid recipe access level update details."),
  INVALID_RECIPE_DETAILS_004(HttpStatus.BAD_REQUEST, "Invalid recipe deletion details."),

  RECIPE_ACCESS_DENIED_001(HttpStatus.FORBIDDEN, "Only admins can access this resource."),
  RECIPE_ACCESS_DENIED_002(HttpStatus.FORBIDDEN, "Only admins and the owner can access this resource."),
  RECIPE_ACCESS_DENIED_003(HttpStatus.FORBIDDEN, "Only admins can disable users."),
  RECIPE_ACCESS_DENIED_004(HttpStatus.FORBIDDEN, "Only admins can update user's description."),

  ;

  public static final String NAME_ERROR_MESSAGE_001 = "Name must be provided and not empty.";

  public static final String DESCRIPTION_ERROR_MESSAGE_001 = "Description must be provided and not empty.";

  public static final String ACCESS_LEVEL_ERROR_MESSAGE_001 = "Access level must be provided when operation is UPDATE.";

  public static final String ACCESS_LEVEL_OPERATION_ERROR_MESSAGE_001 = "Access level cannot be cleared.";

  public static final String RECIPE_IDS_ERROR_MESSAGE_001 = "Recipe ids must be positive integers.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
