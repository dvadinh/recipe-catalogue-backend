package com.dvaults.recipecatalogue.modules.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;

@Entity(name = "linked_oauth2_accounts")
@Table(name = "linked_oauth2_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedOAuth2Account {

  @EmbeddedId
  private LinkedOAuth2AccountId id;

  @Column(columnDefinition = "TEXT")
  private String accessTokenType;

  @Column(columnDefinition = "TEXT")
  private String accessTokenValue;

  private Instant accessTokenIssuedAt;

  private Instant accessTokenExpiresAt;

  private String accessTokenScopes;

  @Column(columnDefinition = "TEXT")
  private String refreshTokenValue;

  private Instant refreshTokenIssuedAt;

  private Instant refreshTokenExpiresAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

}
