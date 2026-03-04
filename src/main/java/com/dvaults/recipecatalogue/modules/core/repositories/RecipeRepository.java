package com.dvaults.recipecatalogue.modules.core.repositories;

import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  @Query("SELECT r " +
      "FROM recipes r " +
      "LEFT JOIN FETCH r.owner o " +
      "LEFT JOIN FETCH r.sections s " +
      "WHERE r.id = :id "
  )
  Optional<Recipe> findByIdFetchOwnerAndSections(long id);

  @Query("SELECT r " +
      "FROM recipes r " +
      "LEFT JOIN FETCH r.owner o " +
      "LEFT JOIN FETCH r.sections s " +
      "LEFT JOIN r.beneficiaries b " +
      "WHERE r.accessLevel = 'PUBLIC'" +
          "OR o.id = :userId " +
          "OR b.user.id = :userId "
  )
  List<Recipe> findAllAccessibleByUserIdFetchOwnerAndSections(long userId);

  @Query("SELECT r " +
      "FROM recipes r " +
      "WHERE r.id IN :ids "
  )
  List<Recipe> findAllByIdsIn(List<Long> ids);

  @Query("SELECT r " +
      "FROM recipes r " +
      "LEFT JOIN FETCH r.beneficiaries b " +
      "WHERE r.id = :id "
  )
  Optional<Recipe> findByIdFetchBeneficiaries(long id);

}
