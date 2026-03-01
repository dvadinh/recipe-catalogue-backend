package com.dvaults.recipecatalogue.modules.core.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "sections")
@Table(
    name = "sections",
    uniqueConstraints = {
        @UniqueConstraint(name = Section.RECIPE_ID_NUMBER_UNIQUE_CONSTRAINT, columnNames = {"recipe_id", "number"}),
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Section {

  public static final String RECIPE_ID_NUMBER_UNIQUE_CONSTRAINT = "unique_constraint_sections_recipe_id_number";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private Integer number;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(columnDefinition = "TEXT")
  private String mediaContentS3Key;

  @Column(columnDefinition = "TEXT")
  private String mediaContentName;

  @Column(columnDefinition = "TEXT")
  private String mediaContentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipe_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Recipe recipe;

  @OneToMany(
      mappedBy = "section",
      cascade = CascadeType.REMOVE
  )
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Builder.Default
  private Set<Step> steps = new HashSet<>();

}
