package com.dvaults.recipecatalogue.security.authentication.basic;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.UsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.tokens.SignUpBasicAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class SignUpBasicAuthenticationProvider implements AuthenticationProvider {

  private final SignInBasicAuthenticationProvider signInBasicAuthenticationProvider;
  private final AuthenticationService authenticationService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    if (!StringUtils.hasText(username))
      throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_005);

    Object password = authentication.getCredentials();
    if (password == null
        || !StringUtils.hasText(password.toString())
    ) throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_006);

    authenticationService.signUpByUsername(
        new UsernamePasswordRequest(
            username,
            password.toString()
        )
    );

    return signInBasicAuthenticationProvider.authenticate(authentication);

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(SignUpBasicAuthenticationToken.class);
  }

}
