package com.dvaults.recipecatalogue.modules.auth.models;

import com.dvaults.recipecatalogue.modules.core.models.Beneficiary;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

import static com.dvaults.recipecatalogue.modules.auth.models.User.DISPLAY_NAME_UNIQUE_CONSTRAINT;
import static com.dvaults.recipecatalogue.modules.auth.models.User.USERNAME_UNIQUE_CONSTRAINT;

@Entity(name = "users")
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = USERNAME_UNIQUE_CONSTRAINT, columnNames = "username"),
        @UniqueConstraint(name = DISPLAY_NAME_UNIQUE_CONSTRAINT, columnNames = "display_name")
    },
    indexes = {
        @Index(name = "index_users_username", columnList = "username"),
        @Index(name = "index_users_type", columnList = "type")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

  public static final String USERNAME_UNIQUE_CONSTRAINT = "unique_constraint_users_username";

  public static final String DISPLAY_NAME_UNIQUE_CONSTRAINT = "unique_constraint_users_display_name";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String username;

  @Column(columnDefinition = "TEXT")
  private String password;

  @Column(columnDefinition = "TEXT")
  private String displayName;

  @Column(columnDefinition = "TEXT")
  private String description;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private Authority type;

  private Boolean enabled;

  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.REMOVE
  )
  @Builder.Default
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<LinkedOAuth2Account> linkedOAuth2Accounts = new HashSet<>();

  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.REMOVE
  )
  @Builder.Default
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Beneficiary> beneficiaries = new HashSet<>();

  public boolean isEnabled() {
    return Boolean.TRUE.equals(enabled);
  }

  public Set<Authority> getAuthorities() {
    return type == null ? Set.of() : Set.of(type);
  }

}
