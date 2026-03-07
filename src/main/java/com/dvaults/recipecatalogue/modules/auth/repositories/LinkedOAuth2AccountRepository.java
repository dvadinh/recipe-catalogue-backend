package com.dvaults.recipecatalogue.modules.auth.repositories;

import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2Account;
import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2AccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LinkedOAuth2AccountRepository extends JpaRepository<LinkedOAuth2Account, LinkedOAuth2AccountId> {

  @Query("SELECT loa " +
      "FROM linked_oauth2_accounts loa " +
      "LEFT JOIN FETCH loa.user " +
      "WHERE loa.id = :id "
  )
  Optional<LinkedOAuth2Account> findByIdFetchUser(LinkedOAuth2AccountId id);

  @Query("SELECT loa " +
      "FROM linked_oauth2_accounts loa " +
      "LEFT JOIN FETCH loa.user " +
      "WHERE loa.user.id = :userId " +
          "AND loa.id.clientRegistrationId = :clientRegistrationId "
  )
  Optional<LinkedOAuth2Account> findByUserIdAndClientRegistrationIdFetchUser(long userId, String clientRegistrationId);
}
