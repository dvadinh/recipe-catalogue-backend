package com.dvaults.recipecatalogue.modules.core.errors;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MediaErrorDictionary implements ErrorDictionaryDescriptor {

  INVALID_MEDIA_DETAILS_001(HttpStatus.BAD_REQUEST, "Multipart file media not found or empty."),
  INVALID_MEDIA_DETAILS_002(HttpStatus.BAD_REQUEST, "The media cannot be parsed from request."),
  INVALID_MEDIA_DETAILS_003(HttpStatus.BAD_GATEWAY, "Cannot upload media content to AWS S3. Please try again later."),

  ;

  public static final String MEDIA_ERROR_MESSAGE_001 = "Media must be provided and not empty.";

  private final HttpStatus status;
  private final Object details;

  @Override
  public String getErrorId() {
    return name();
  }

}
