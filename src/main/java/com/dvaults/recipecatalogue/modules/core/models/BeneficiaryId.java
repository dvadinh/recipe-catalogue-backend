package com.dvaults.recipecatalogue.modules.core.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryId implements Serializable {

  private Long userId;

  private Long recipeId;

}
