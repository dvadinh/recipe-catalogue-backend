package com.dvaults.recipecatalogue.security.authentication.tokens;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class SignInBasicAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public SignInBasicAuthenticationToken(UsernamePasswordAuthenticationToken authenticationToken) {
    super(authenticationToken.getPrincipal(), authenticationToken.getCredentials());
  }

  public SignInBasicAuthenticationToken(
      Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities
  ) {
    super(principal, credentials, authorities);
  }

}
