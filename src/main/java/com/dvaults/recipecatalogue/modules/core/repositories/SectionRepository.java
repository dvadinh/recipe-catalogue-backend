package com.dvaults.recipecatalogue.modules.core.repositories;

import com.dvaults.recipecatalogue.modules.core.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

  @Query("SELECT s " +
      "FROM sections s " +
      "LEFT JOIN FETCH s.recipe r " +
      "WHERE s.id = :id "
  )
  Optional<Section> findByIdFetchRecipe(long id);

}
