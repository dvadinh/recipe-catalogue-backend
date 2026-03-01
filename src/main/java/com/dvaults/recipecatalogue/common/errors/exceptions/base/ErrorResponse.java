package com.dvaults.recipecatalogue.common.errors.exceptions.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ErrorResponse {

  private int status;

  private String errorId;

  private Object details;

}
