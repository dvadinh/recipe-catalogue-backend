package com.dvaults.recipecatalogue.security.authentication.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

  @ToString.Exclude
  private final UserPrincipal principal;

  private final Object credentials;

  public UserAuthenticationToken(
      UserPrincipal principal,
      Object details,
      Object credentials
  ) {
    super(principal.getAuthorities());
    this.principal = principal;
    this.credentials = credentials;
    setDetails(details);
    setAuthenticated(true);
  }

  @ConstructorProperties({
      "principal",
      "authorities",
      "details",
      "credentials",
      "authenticated"
  })
  public UserAuthenticationToken(
      UserPrincipal principal,
      Collection<? extends GrantedAuthority> authorities,
      Object details,
      Object credentials,
      boolean authenticated
  ) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    setDetails(details);
    setAuthenticated(authenticated);
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return new HashSet<>(principal.getAuthorities());
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

}
