package com.dvaults.recipecatalogue.modules.core.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum StepErrorDictionary implements ErrorDictionaryDescriptor {

  STEP_NOT_FOUND_001(HttpStatus.NOT_FOUND, "Step not found."),

  ;

  public static final String TITLE_ERROR_MESSAGE_001 = "Step title must be provided and not empty.";
  public static final String DESCRIPTION_ERROR_MESSAGE_001 = "Step description must be provided and not empty.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
