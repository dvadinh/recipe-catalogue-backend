package com.dvaults.recipecatalogue.common.utils;

import java.util.Map;

public class ValidationUtils {

  public static void constructErrorFromMessage(
      Map<String, String> errors,
      String fieldName,
      String message
  ) {
    errors.merge(
        fieldName,
        message,
        (existing, next) -> String.format("%s %s", existing, next));
  }

}
