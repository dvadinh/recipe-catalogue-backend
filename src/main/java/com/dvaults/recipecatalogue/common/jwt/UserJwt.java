package com.dvaults.recipecatalogue.common.jwt;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJwt {

  private String sub;

  private Authority type;

  private String username;

  private String displayName;

  private Boolean enabled;

  private String jti;

  private Instant iat;

  private Instant exp;

  public Map<String, Object> toClaims() {

    Map<String, Object> claims = new LinkedHashMap<>();

    claims.put("sub", getSub());
    claims.put("type", getType());
    claims.put("username", getUsername());
    claims.put("displayName", getDisplayName());
    claims.put("enabled", getEnabled());
    claims.put("jti", getJti());
    claims.put("iat", Date.from(getIat()));
    claims.put("exp", Date.from(getExp()));

    return claims;

  }

}
