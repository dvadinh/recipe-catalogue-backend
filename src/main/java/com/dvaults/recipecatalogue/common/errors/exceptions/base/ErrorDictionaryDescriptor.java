package com.dvaults.recipecatalogue.common.errors.exceptions.base;

import org.springframework.http.HttpStatus;

public interface ErrorDictionaryDescriptor {

  HttpStatus getStatus();

  String getErrorId();

  Object getDetails();

}
