package com.dvaults.recipecatalogue.modules.core.models;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
import java.util.HashSet;
import java.util.Set;

@Entity(name = "recipes")
@Table(
    name = "recipes",
    indexes = {
        @Index(name = "index_recipes_owner_id", columnList = "owner_id"),
        @Index(name = "index_recipes_name", columnList = "name")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(columnDefinition = "TEXT")
  private String mediaContentS3Key;

  @Column(columnDefinition = "TEXT")
  private String mediaContentName;

  @Column(columnDefinition = "TEXT")
  private String mediaContentType;

  private Instant lastUpdatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User owner;

  @OneToMany(
      mappedBy = "recipe",
      cascade = CascadeType.REMOVE
  )
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Builder.Default
  private Set<Beneficiary> beneficiaries = new HashSet<>();

  @OneToMany(
      mappedBy = "recipe",
      cascade = CascadeType.REMOVE
  )
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Builder.Default
  private Set<Section> sections = new HashSet<>();

}
