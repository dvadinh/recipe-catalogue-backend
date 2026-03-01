package com.dvaults.recipecatalogue.modules.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import static com.dvaults.recipecatalogue.modules.core.models.Step.SECTION_ID_NUMBER_UNIQUE_CONSTRAINT;

@Entity(name = "steps")
@Table(
    name = "steps",
    uniqueConstraints = {
        @UniqueConstraint(name = SECTION_ID_NUMBER_UNIQUE_CONSTRAINT, columnNames = {"section_id", "number"}),
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Step {

  public static final String SECTION_ID_NUMBER_UNIQUE_CONSTRAINT = "unique_constraint_steps_section_id_number";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
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
  @JoinColumn(name = "section_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Section section;

}
