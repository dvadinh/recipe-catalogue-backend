package com.dvaults.recipecatalogue.modules.core.repositories;

import com.dvaults.recipecatalogue.modules.core.models.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

  @Query("SELECT s " +
      "FROM steps s " +
      "WHERE s.section.id IN :sectionIds "
  )
  List<Step> findAllBySectionIdsIn(List<Long> sectionIds);

  @Query("SELECT s " +
      "FROM steps s " +
      "WHERE s.section.id = :sectionId "
  )
  List<Step> findAllBySectionId(long sectionId);

  @Query("SELECT s " +
      "FROM steps s " +
      "LEFT JOIN FETCH s.section st " +
      "LEFT JOIN FETCH st.recipe r " +
      "WHERE s.id = :id "
  )
  Optional<Step> findByIdFetchSectionRecipe(long id);

}
