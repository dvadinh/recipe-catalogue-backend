package com.dvaults.recipecatalogue.common.utils;

import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorDictionaryDescriptor;
import com.dvaults.recipecatalogue.common.errors.exceptions.base.ErrorResponse;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.UsernamePasswordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  public static String getQueryParameterValue(
      SavedRequest savedRequest,
      String queryParameterName
  ) {
    return getQueryParameterValue(savedRequest, queryParameterName, 0);
  }

  public static String getQueryParameterValue(
      SavedRequest savedRequest,
      String queryParameterName,
      int index
  ) {

    if (savedRequest == null || savedRequest.getParameterMap() == null) {
      return null;
    }

    String[] queryParameterValues = savedRequest.getParameterMap().get(queryParameterName);
    if (queryParameterValues != null && queryParameterValues.length > index) {
      return queryParameterValues[index];
    }

    return null;

  }

  public static String getQueryParameterValue(
      HttpServletRequest request,
      String queryParameterName
  ) {
    return getQueryParameterValue(request, queryParameterName, 0);
  }

  public static String getQueryParameterValue(
      HttpServletRequest request,
      String queryParameterName,
      int index
  ) {

    String[] queryParameterValues = request.getParameterValues(queryParameterName);
    if (queryParameterValues != null && queryParameterValues.length > index) {
      return queryParameterValues[index];
    }

    return null;

  }

  public static void writeAuthenticationErrorResponse(
      HttpServletResponse response,
      RuntimeException exception
  ) throws IOException {
    writeErrorResponse(
        response,
        exception,
        HttpStatus.UNAUTHORIZED.value(),
        "UNAUTHORIZED_ACCESS"
    );
  }

  public static void writeAuthorizationErrorResponse(
      HttpServletResponse response,
      RuntimeException exception
  ) throws IOException {
    writeErrorResponse(
        response,
        exception,
        HttpStatus.FORBIDDEN.value(),
        "FORBIDDEN_ACCESS"
    );
  }

  public static void writeErrorResponse(
      HttpServletResponse response,
      RuntimeException exception,
      int status,
      String errorId
  ) throws IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
        .status(status)
        .errorId(errorId)
        .details(exception.getLocalizedMessage())
        .build())
    );
    response.flushBuffer();
  }

  public static void writeErrorResponse(
      HttpServletResponse response,
      ErrorDictionaryDescriptor errorDictionaryDescriptor
  ) throws IOException {
    writeErrorResponse(response, errorDictionaryDescriptor, null);
  }

  public static void writeErrorResponse(
      HttpServletResponse response,
      ErrorDictionaryDescriptor errorDictionaryDescriptor,
      Object details
  ) throws IOException {

    int status = errorDictionaryDescriptor.getStatus().value();

    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
        .status(status)
        .errorId(errorDictionaryDescriptor.getErrorId())
        .details(details == null ? errorDictionaryDescriptor.getDetails() : details)
        .build())
    );
    response.flushBuffer();

  }

  public static Pair<Boolean, Map<String, String>> screenUsernamePassword(UsernamePasswordRequest usernamePasswordRequest) {

    Pair<Boolean, String> usernameValidation = screenUsername(usernamePasswordRequest.username());
    Pair<Boolean, String> passwordValidation = screenPassword(usernamePasswordRequest.password());
    boolean isUsernameValid = usernameValidation.getFirst();
    boolean isPasswordValid = passwordValidation.getFirst();

    Map<String, String> errors = new HashMap<>();
    boolean validUsernamePassword = isUsernameValid && isPasswordValid;
    if (!validUsernamePassword) {
      if (!isUsernameValid) errors.put("username", usernameValidation.getSecond());
      if (!isPasswordValid) errors.put("password", passwordValidation.getSecond());
    }

    return Pair.of(validUsernamePassword, errors);

  }

  private static void appendError(
      StringBuilder errors,
      String error
  ) {
    if (!errors.isEmpty()) {
      errors.append(" ");
    }
    errors.append(error);
  }

  public static Pair<Boolean, String> screenUsername(String username) {

    boolean isValid = true;

    StringBuilder errors = new StringBuilder();
    String trimmedUsername = username.trim();
    if (!StringUtils.hasText(trimmedUsername)) {
      appendError(errors, "Username cannot be blank.");
      isValid = false;
    } else if (trimmedUsername.length() < 8 || trimmedUsername.length() > 255) {
      appendError(errors, "Username must be between 8 and 255 characters long.");
      isValid = false;
    }

    return Pair.of(isValid, errors.toString());

  }

  public static Pair<Boolean, String> screenPassword(String password) {

    boolean isValid = true;

    StringBuilder errors = new StringBuilder();
    String trimmedPassword = password.trim();
    if (!StringUtils.hasText(trimmedPassword)) {
      appendError(errors, "Password cannot be blank.");
      isValid = false;
    } else if (trimmedPassword.length() < 8 || trimmedPassword.length() > 255) {
      appendError(errors, "Password must be between 8 and 255 characters long.");
      isValid = false;
    }

    return Pair.of(isValid, errors.toString());

  }

}
