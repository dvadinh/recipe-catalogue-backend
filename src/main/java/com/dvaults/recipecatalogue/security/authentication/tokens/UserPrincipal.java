package com.dvaults.recipecatalogue.security.authentication.tokens;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticatedPrincipal;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPrincipal implements AuthenticatedPrincipal, Principal, Serializable {

  private final Long id;

  private final String username;

  private final String displayName;

  private final Set<Authority> authorities;

  private final Boolean enabled;

  @ConstructorProperties({
      "id",
      "username",
      "displayName",
      "authorities",
      "enabled"
  })
  public UserPrincipal(
      Long id,
      String username,
      String displayName,
      Set<Authority> authorities,
      Boolean enabled
  ) {
    this.id = id;
    this.username = username;
    this.displayName = displayName;
    this.authorities = authorities;
    this.enabled = enabled;
  }

  @Override
  public String getName() {
    return getUsername();
  }

  public Authority getAuthority() {
    return authorities == null ? null : authorities.stream().findFirst().orElse(null);
  }

}
