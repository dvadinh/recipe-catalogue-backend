package com.dvaults.recipecatalogue.modules.core.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SectionErrorDictionary implements ErrorDictionaryDescriptor {

  SECTION_NOT_FOUND_001(HttpStatus.NOT_FOUND, "Section not found."),

  SECTION_ACCESS_DENIED_001(HttpStatus.FORBIDDEN, "Only admins and the recipe owner can update its section."),

  ;

  public static final String TITLE_ERROR_MESSAGE_001 = "Section title must be provided and not empty.";
  public static final String DESCRIPTION_ERROR_MESSAGE_001 = "Section description must be provided and not empty.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
