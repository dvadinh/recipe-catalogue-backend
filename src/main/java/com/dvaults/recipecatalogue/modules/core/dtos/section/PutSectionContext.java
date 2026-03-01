package com.dvaults.recipecatalogue.modules.core.dtos.section;

import com.dvaults.recipecatalogue.common.dtos.PutMutation;
import com.dvaults.recipecatalogue.modules.core.dtos.step.PutStepContext;
import com.dvaults.recipecatalogue.modules.core.models.Section;

import java.util.List;

public record PutSectionContext(

    PutMutation<PutSection, Section> sectionMutation,

    List<PutStepContext> stepContexts

) {
}
