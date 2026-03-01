package com.dvaults.recipecatalogue.security.authentication.basic;

import com.dvaults.recipecatalogue.configs.BasicAuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.tokens.SignInBasicAuthenticationToken;
import com.dvaults.recipecatalogue.security.authentication.tokens.SignUpBasicAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

public class BasicAuthenticationTokenConverter extends BasicAuthenticationConverter {

  @Override
	public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {

    String requestUri = request.getRequestURI();

    if (BasicAuthenticationConfigs.SIGN_UP_URI.equals(requestUri)) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = super.convert(request);
      if (usernamePasswordAuthenticationToken == null) {
        throw new BasicAuthenticationException(AuthenticationErrorDictionary.BASIC_AUTHENTICATION_NOT_FOUND_001);
      }
      return new SignUpBasicAuthenticationToken(usernamePasswordAuthenticationToken);

    } else if (BasicAuthenticationConfigs.SIGN_IN_URI.equals(requestUri)) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = super.convert(request);
      if (usernamePasswordAuthenticationToken == null) {
        throw new BasicAuthenticationException(AuthenticationErrorDictionary.BASIC_AUTHENTICATION_NOT_FOUND_001);
      }
      return new SignInBasicAuthenticationToken(usernamePasswordAuthenticationToken);

    } else {
      throw new BasicAuthenticationException(AuthenticationErrorDictionary.UNEXPECTED_AUTHENTICATION_ERROR_001);
    }

	}

}
