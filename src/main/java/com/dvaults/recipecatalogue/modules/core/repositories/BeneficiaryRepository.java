package com.dvaults.recipecatalogue.modules.core.repositories;

import com.dvaults.recipecatalogue.modules.core.models.Beneficiary;
import com.dvaults.recipecatalogue.modules.core.models.BeneficiaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, BeneficiaryId> {
}
