package com.dvaults.recipecatalogue.modules.core.mappers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.modules.core.dtos.section.requests.PutSectionRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.section.responses.SectionResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.step.responses.StepResponse;
import com.dvaults.recipecatalogue.modules.core.models.Section;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.Comparator;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    uses = {
        StepMapper.class
    }
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class SectionMapper {

  private StepMapper stepMapper;
  private CommonMapper commonMapper;

  @Named("mapToMediaResponse")
  protected MediaResponse mapToMediaResponse(Section section) {

    if (section.getMediaContentS3Key() == null) {
      return null;
    }

    return commonMapper.toMediaResponse(
        section.getMediaContentS3Key(),
        section.getMediaContentName(),
        section.getMediaContentType()
    );

  }

  @Named("mapToSectionResponse")
  @Mapping(target = "media", source = "section", qualifiedByName = "mapToMediaResponse")
  @Mapping(target = "steps", source = "stepResponseList")
  public abstract SectionResponse mapToSectionResponse(
      Section section,
      List<StepResponse> stepResponseList
  );

  @Named("toSectionResponse")
  public SectionResponse toSectionResponse(
      Section section,
      List<Step> stepList
  ) {
    return mapToSectionResponse(
        section,
        stepList.stream()
            .sorted(Comparator.comparingInt(Step::getNumber))
            .map(stepMapper::toStepResponse)
            .toList()
    );
  }

  @Named("screenPutSectionRequest")
  @Mapping(
      target = "title",
      expression = "java(putSectionRequest.title() == null ? null : putSectionRequest.title().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(putSectionRequest.description() == null ? null : putSectionRequest.description().trim())"
  )
  @Mapping(target = "steps", qualifiedByName = "screenPutStepRequestList")
  public abstract PutSectionRequest screenPutSectionRequest(PutSectionRequest putSectionRequest);

  @Named("screenPutSectionRequestList")
  @IterableMapping(qualifiedByName = "screenPutSectionRequest")
  public abstract List<PutSectionRequest> screenPutSectionRequestList(List<PutSectionRequest> putSectionRequestList);

}
