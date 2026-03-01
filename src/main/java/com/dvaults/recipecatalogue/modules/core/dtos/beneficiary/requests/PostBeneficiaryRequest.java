package com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests;

import java.util.List;

public record PostBeneficiaryRequest(

    List<Long> userIds

) {
}
