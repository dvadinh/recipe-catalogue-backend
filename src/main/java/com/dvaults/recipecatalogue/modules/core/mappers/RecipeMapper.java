package com.dvaults.recipecatalogue.modules.core.mappers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.DeleteRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeDetailsResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.section.responses.SectionResponse;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import lombok.AccessLevel;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    uses = {
        UserMapper.class,
        SectionMapper.class
    }
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class RecipeMapper {

  private SectionMapper sectionMapper;
  private CommonMapper commonMapper;

  @Named("mapToMediaResponse")
  protected MediaResponse mapToMediaResponse(Recipe recipe) {

    if (recipe.getMediaContentS3Key() == null) {
      return null;
    }

    return commonMapper.toMediaResponse(
        recipe.getMediaContentS3Key(),
        recipe.getMediaContentName(),
        recipe.getMediaContentType()
    );

  }

  @Named("mapToRecipeDetailsResponse")
  @Mapping(target = "media", source = "recipe", qualifiedByName = "mapToMediaResponse")
  @Mapping(target = "owner", source = "recipe.owner", qualifiedByName = "toUserSummaryResponse")
  @Mapping(target = "sections", source = "sectionResponseList")
  protected abstract RecipeDetailsResponse mapToRecipeDetailsResponse(
      Recipe recipe,
      List<SectionResponse> sectionResponseList
  );

  @Named("mapToRecipeDetailsResponse")
  public RecipeDetailsResponse toRecipeDetailsResponse(
      Recipe recipe,
      Map<Section, List<Step>> stepsBySectionMap
  ) {
    return mapToRecipeDetailsResponse(
        recipe,
        stepsBySectionMap.entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry<Section, List<Step>>::getKey, Comparator.comparing(Section::getNumber)))
            .map(stepsBySectionEntry -> sectionMapper.toSectionResponse(
                stepsBySectionEntry.getKey(),
                stepsBySectionEntry.getValue()))
            .toList()
    );
  }

  @Named("toRecipeSummaryResponse")
  public abstract RecipeSummaryResponse toRecipeSummaryResponse(Recipe recipe);

  @Named("screenPostRecipeRequest")
  @Mapping(
      target = "name",
      expression = "java(postRecipeRequest.name() == null ? null : postRecipeRequest.name().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(postRecipeRequest.description() == null ? null : postRecipeRequest.description().trim())"
  )
  public abstract PostRecipeRequest screenPostRecipeRequest(PostRecipeRequest postRecipeRequest);

  public abstract PatchRecipeAccessLevelRequest screenPatchRecipeAccessLevelRequest(PatchRecipeAccessLevelRequest patchRecipeAccessLevelRequest);

  @Named("screenPutRecipeRequest")
  @Mapping(
      target = "name",
      expression = "java(putRecipeRequest.name() == null ? null : putRecipeRequest.name().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(putRecipeRequest.description() == null ? null : putRecipeRequest.description().trim())"
  )
  @Mapping(target = "sections", qualifiedByName = "screenPutSectionRequestList")
  public abstract PutRecipeRequest screenPutRecipeRequest(PutRecipeRequest putRecipeRequest);

  public abstract DeleteRecipeRequest screenDeleteRecipeRequest(DeleteRecipeRequest deleteRecipeRequest);

}
