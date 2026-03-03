package com.dvaults.recipecatalogue.modules.core.mappers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.modules.core.dtos.step.requests.PutStepRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.step.responses.StepResponse;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import lombok.AccessLevel;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class StepMapper {

  private CommonMapper commonMapper;

  @Named("mapToMediaResponse")
  protected MediaResponse mapToMediaResponse(Step step) {

    if (step.getMediaContentS3Key() == null) {
      return null;
    }

    return commonMapper.toMediaResponse(
        step.getMediaContentS3Key(),
        step.getMediaContentName(),
        step.getMediaContentType()
    );

  }

  @Named("toStepResponse")
  @Mapping(target = "media", source = "step", qualifiedByName = "mapToMediaResponse")
  public abstract StepResponse toStepResponse(Step step);

  @Named("screenPutStepRequest")
  @Mapping(
      target = "title",
      expression = "java(putStepRequest.title() == null ? null : putStepRequest.title().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(putStepRequest.description() == null ? null : putStepRequest.description().trim())"
  )
  public abstract PutStepRequest screenPutStepRequest(PutStepRequest putStepRequest);

  @Named("screenPutStepRequestList")
  @IterableMapping(qualifiedByName = "screenPutStepRequest")
  public abstract List<PutStepRequest> screenPutStepRequestList(List<PutStepRequest> putStepRequestList);

}
