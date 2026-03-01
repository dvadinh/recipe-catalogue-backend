package com.dvaults.recipecatalogue.modules.core.models;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "beneficiaries")
@Table(
    name = "beneficiaries",
    indexes = {
        @Index(name = "index_beneficiaries_user_id", columnList = "user_id"),
        @Index(name = "index_beneficiaries_recipe_id", columnList = "recipe_id")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Beneficiary {

  @EmbeddedId
  private BeneficiaryId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("recipeId")
  @JoinColumn(name = "recipe_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Recipe recipe;

}
