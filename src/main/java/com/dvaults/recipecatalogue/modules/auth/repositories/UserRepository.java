package com.dvaults.recipecatalogue.modules.auth.repositories;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u " +
      "FROM users u " +
      "WHERE u.username = :username "
  )
  Optional<User> findByUsername(String username);

  @Query("SELECT u " +
      "FROM users u " +
      "WHERE u.displayName = :displayName "
  )
  Optional<User> findByDisplayName(String displayName);

  @Query("SELECT u " +
      "FROM users u " +
      "LEFT JOIN FETCH u.linkedOAuth2Accounts loa " +
      "WHERE u.id = :id "
  )
  Optional<User> findByIdFetchLinkedOAuth2Accounts(long id);

  @Query("SELECT u " +
      "FROM users u " +
      "WHERE u.id IN :ids "
  )
  List<User> findAllByIdIn(List<Long> ids);

}
