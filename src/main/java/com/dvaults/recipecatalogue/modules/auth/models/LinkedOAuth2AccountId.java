package com.dvaults.recipecatalogue.modules.auth.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LinkedOAuth2AccountId implements Serializable {

  private String clientRegistrationId;

  private String principalName;

}
