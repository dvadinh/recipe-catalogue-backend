package com.dvaults.recipecatalogue.common.errors.handlers;

import com.dvaults.recipecatalogue.common.errors.exceptions.CookieAwareException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.BaseException;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_ERROR_ID = "DATA_INTEGRITY_VIOLATION_001";

  public static final String CONSTRAINT_VIOLATION_EXCEPTION_DEFAULT_DETAILS = "A constraint violation occurs. Please try again later.";

  public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_DEFAULT_DETAILS = "A data integrity violation occurs. Please try again later.";

  public static final Map<String, String> UNIQUE_CONSTRAINT_VIOLATION_DETAILS_BY_CONSTRAINT_NAME = Map.ofEntries(

      Map.entry(User.USERNAME_UNIQUE_CONSTRAINT, "Username already exists."),
      Map.entry(User.DISPLAY_NAME_UNIQUE_CONSTRAINT, "Display name already exists."),

      Map.entry(Step.SECTION_ID_NUMBER_UNIQUE_CONSTRAINT, "A step with that number already exists in this recipe."),
      Map.entry(Section.RECIPE_ID_NUMBER_UNIQUE_CONSTRAINT, "A section with that number already exists in this step.")

  );

  @Override
  public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      @NonNull HttpRequestMethodNotSupportedException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      @NonNull HttpMediaTypeNotSupportedException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleMissingServletRequestParameter(
      @NonNull MissingServletRequestParameterException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleServletRequestBindingException(
      @NonNull ServletRequestBindingException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleNoResourceFoundException(
      @NonNull NoResourceFoundException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleNoHandlerFoundException(
      @NonNull NoHandlerFoundException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return handleBuiltinException(exception, null, headers, status, request);
  }

  @Override
  public ResponseEntity<Object> handleTypeMismatch(
      @NonNull TypeMismatchException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {
    return ResponseEntity.status(status)
        .body(ErrorResponse.builder()
            .status(status.value())
            .errorId("TYPE_MISMATCH_ERROR")
            .details(String.format(
                "Parameter '%s' must be of type '%s', but received value %s",
                exception.getPropertyName(),
                exception.getRequiredType() != null
                    ? exception.getRequiredType().getSimpleName()
                    : Object.class.getSimpleName(),
                exception.getValue()))
            .build());
  }

  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {

    Throwable cause = exception.getMostSpecificCause();

    if (cause instanceof JsonParseException jsonParseException) {
      return ResponseEntity.status(status)
          .body(ErrorResponse.builder()
              .status(status.value())
              .errorId("JSON_PARSE_ERROR")
              .details(jsonParseException.getOriginalMessage())
              .build());

    } else if (cause instanceof InvalidFormatException invalidFormatException) {
      Class<?> targetType = invalidFormatException.getTargetType();
      return ResponseEntity.status(status)
          .body(ErrorResponse.builder()
              .status(status.value())
              .errorId("INVALID_FORMAT_ERROR")
              .details(invalidFormatException.getOriginalMessage()
                  .replace(targetType.getCanonicalName(), targetType.getSimpleName())
                  .split(":")[0])
              .build());

    } else {
      return handleBuiltinException(
          exception,
          createProblemDetail(
              exception,
              status,
              cause.getLocalizedMessage()
                  .split(":")[0],
              null,
              null,
              request),
          headers,
          status,
          request
      );
    }

  }

  public <T extends Exception> ResponseEntity<Object> handleBuiltinException(
      T exception,
      Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request
  ) {

    log.info("Built-in exception caught: {}", exception.toString());

    ProblemDetail responseBody = (ProblemDetail) Objects.requireNonNull(super.handleExceptionInternal(
            exception,
            body,
            headers,
            status,
            request))
        .getBody();

    assert responseBody != null;
    log.info("Built-in response: {}", responseBody);
    String title = responseBody.getTitle();

    return ResponseEntity.status(status)
        .body(ErrorResponse.builder()
            .status(status.value())
            .errorId((title == null ? exception.getClass().getSimpleName() : title)
                .replace(" ", "_")
                .toUpperCase())
            .details(responseBody.getDetail())
            .build());

  }

  @ExceptionHandler(BaseException.class)
  private <T extends BaseException> ResponseEntity<ErrorResponse> handleBaseException(
      T exception,
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    log.info("Exception caught: {}", exception.toString());

    if (exception instanceof CookieAwareException cookieAwareException) {
      cookieAwareException.getCookies()
          .forEach(cookie -> response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString()));
    }

    return ResponseEntity.status(exception.getStatus())
        .body(ErrorResponse.builder()
            .status(exception.getStatus())
            .errorId(exception.getErrorId())
            .details(exception.getDetails())
            .build());

  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  private ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException exception,
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    log.info("Data integrity violation exception caught: {}", exception.toString());

    if (NestedExceptionUtils.getRootCause(exception) instanceof ConstraintViolationException constraintViolationException) {
      return ResponseEntity.status(HttpStatus.CONFLICT.value())
          .body(ErrorResponse.builder()
              .status(HttpStatus.CONFLICT.value())
              .errorId(DATA_INTEGRITY_VIOLATION_EXCEPTION_ERROR_ID)
              .details(UNIQUE_CONSTRAINT_VIOLATION_DETAILS_BY_CONSTRAINT_NAME.getOrDefault(
                  constraintViolationException.getConstraintName(),
                  CONSTRAINT_VIOLATION_EXCEPTION_DEFAULT_DETAILS))
              .build());

    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT.value())
          .body(ErrorResponse.builder()
              .status(HttpStatus.CONFLICT.value())
              .errorId(DATA_INTEGRITY_VIOLATION_EXCEPTION_ERROR_ID)
              .details(DATA_INTEGRITY_VIOLATION_EXCEPTION_DEFAULT_DETAILS)
              .build());
    }

  }

}
