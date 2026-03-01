package com.dvaults.recipecatalogue.modules.auth.models;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

  USER,
  ADMIN;

  @Override
  public String getAuthority() {
    return this.name();
  }

}
