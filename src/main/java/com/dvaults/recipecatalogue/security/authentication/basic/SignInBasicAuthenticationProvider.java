package com.dvaults.recipecatalogue.security.authentication.basic;

import com.dvaults.recipecatalogue.modules.auth.mappers.AuthenticationMapper;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import com.dvaults.recipecatalogue.security.authentication.tokens.SignInBasicAuthenticationToken;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Setter
public class SignInBasicAuthenticationProvider extends DaoAuthenticationProvider {

  private AuthenticationMapper authenticationMapper;

  public SignInBasicAuthenticationProvider(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
      AuthenticationMapper authenticationMapper
  ) {
    super(userDetailsService);
    setPasswordEncoder(passwordEncoder);
    setAuthenticationMapper(authenticationMapper);
    setHideUserNotFoundExceptions(false);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(SignInBasicAuthenticationToken.class);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    if (!StringUtils.hasText(username)) throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_005);

    return super.authenticate(authentication);

  }

  @Override
  public void additionalAuthenticationChecks(
      UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication
  ) throws AuthenticationException {

    if (!StringUtils.hasText(userDetails.getPassword())) {
      throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_006);
    }

    if (!getPasswordEncoder().matches((String) authentication.getCredentials(), userDetails.getPassword())) {
      throw new BasicAuthenticationException(AuthenticationErrorDictionary.INVALID_BASIC_AUTHENTICATION_001);
    }

  }

  @Override
  protected Authentication createSuccessAuthentication(
      Object principal,
      Authentication authentication,
      UserDetails userDetails
  ) {
    super.createSuccessAuthentication(
        principal,
        authentication,
        userDetails
    );
    return new UserAuthenticationToken(
        authenticationMapper.toUserPrincipal((User) userDetails),
        null,
        null
    );
  }

}
