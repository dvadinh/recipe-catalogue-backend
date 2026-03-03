package com.dvaults.recipecatalogue.modules.core.services.api.implementation;

import com.dvaults.recipecatalogue.common.dtos.PutMutation;
import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.configs.AwsS3Configs;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.PutRecipeContext;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeDetailsResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.section.PutSection;
import com.dvaults.recipecatalogue.modules.core.dtos.section.PutSectionContext;
import com.dvaults.recipecatalogue.modules.core.dtos.step.PutStep;
import com.dvaults.recipecatalogue.modules.core.dtos.step.PutStepContext;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.dvaults.recipecatalogue.modules.core.repositories.BeneficiaryRepository;
import com.dvaults.recipecatalogue.modules.core.repositories.RecipeRepository;
import com.dvaults.recipecatalogue.modules.core.repositories.SectionRepository;
import com.dvaults.recipecatalogue.modules.core.repositories.StepRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.MediaService;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.RecipeService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  private final MediaService mediaService;

  private final RecipeRepository recipeRepository;
  private final StepRepository stepRepository;
  private final SectionRepository sectionRepository;
  private final BeneficiaryRepository beneficiaryRepository;

  private final RecipeMapper recipeMapper;
  private final CommonMapper commonMapper;

  @Override
  public List<RecipeDetailsResponse> findAllByUserId(long userId) {

    List<Recipe> recipes = recipeRepository.findAllByUserIdFetchOwnerAndSections(userId);

    Map<Long, List<Step>> stepsBySectionId = recipes.stream()
        .flatMap(recipe -> recipe.getSections().stream())
        .collect(Collectors.toMap(
            Section::getId,
            section -> new ArrayList<>()
        ));

    stepRepository.findAllBySectionIdsIn(new ArrayList<>(stepsBySectionId.keySet()))
        .forEach(step -> {
          if (stepsBySectionId.containsKey(step.getSection().getId())) {
            stepsBySectionId.get(step.getSection().getId()).add(step);
          }
        });

    return recipes.stream()
        .map(recipe -> recipeMapper.toRecipeDetailsResponse(
            recipe,
            recipe.getSections()
                .stream()
                .collect(Collectors.toMap(
                    section -> section,
                    section -> stepsBySectionId.get(section.getId())))))
        .toList();

  }

  @Override
  public RecipeDetailsResponse findByRecipe(
      Recipe recipe,
      UserPrincipal principal
  ) {

    Map<Section, List<Step>> stepsBySection = recipe.getSections()
        .stream()
        .collect(Collectors.toMap(
            section -> section,
            section -> new ArrayList<>()
        ));

    stepRepository.findAllBySectionIdsIn(
            recipe.getSections()
                .stream()
                .map(Section::getId)
                .toList())
        .forEach(step -> stepsBySection.get(step.getSection()).add(step));

    return recipeMapper.toRecipeDetailsResponse(
        recipe,
        stepsBySection
    );

  }

  @Override
  @Transactional
  public RecipeDetailsResponse create(
      PostRecipeRequest screenedRequest,
      User principalUser
  ) {
    return recipeMapper.toRecipeDetailsResponse(
        recipeRepository.save(
            Recipe.builder()
                .owner(principalUser)
                .name(screenedRequest.name())
                .description(screenedRequest.description())
                .build()),
        Map.of()
    );
  }

  @Override
  @Transactional
  public RecipeDetailsResponse updateByRecipeAndPutRecipeItems(
      Recipe recipe,
      PutRecipeContext putRecipeContext,
      UserPrincipal principal
  ) {

    recipe.setName(putRecipeContext.recipeMutation().request().name());
    recipe.setDescription(putRecipeContext.recipeMutation().request().description());
    recipe.setLastUpdatedAt(Instant.now());

    Set<Long> sectionIdsToUpdated = new HashSet<>();
    List<Section> sectionsToBeCreated = new ArrayList<>();
    Map<Integer, Section> finalSectionsByIndex = new HashMap<>();
    List<String> mediaS3KeysToBeDeleted = new ArrayList<>();
    for (int putSectionIndex = 0; putSectionIndex < putRecipeContext.sectionContexts().size(); putSectionIndex++) {

      PutSectionContext putSectionContext = putRecipeContext.sectionContexts().get(putSectionIndex);
      PutMutation<PutSection, Section> putSectionMutation = putSectionContext.sectionMutation();
      Section section;

      if (putSectionMutation.isNew()) {
        section = Section.builder()
            .recipe(recipe)
            .title(putSectionMutation.request().title())
            .description(putSectionMutation.request().description())
            .number(putSectionMutation.number())
            .build();
        sectionsToBeCreated.add(section);
      } else {
        section = putSectionMutation.entity();
        sectionIdsToUpdated.add(section.getId());
      }

      finalSectionsByIndex.put(putSectionIndex, section);

    }

    List<Long> sectionIdsToBeDeleted = recipe.getSections()
        .stream()
        .map(Section::getId)
        .filter(sectionId -> !sectionIdsToUpdated.contains(sectionId))
        .toList();
    if (!sectionIdsToBeDeleted.isEmpty()) {
      mediaS3KeysToBeDeleted.addAll(
          recipe.getSections()
              .stream()
              .filter(section -> sectionIdsToBeDeleted.contains(section.getId()))
              .map(Section::getMediaContentS3Key)
              .filter(StringUtils::hasText)
              .toList()
      );
      sectionRepository.deleteAllById(sectionIdsToBeDeleted);
    }

    int sectionSize = putRecipeContext.sectionContexts().size();
    for (int putSectionIndex = 0; putSectionIndex < sectionSize; putSectionIndex++) {

      PutSectionContext putSectionContext = putRecipeContext.sectionContexts().get(putSectionIndex);
      PutMutation<PutSection, Section> putSectionMutation = putSectionContext.sectionMutation();

      if (!putSectionMutation.isNew()) {
        Section section = putSectionMutation.entity();
        section.setNumber(-sectionSize + putSectionIndex);
      }

    }
    sectionRepository.flush();

    for (int putSectionIndex = 0; putSectionIndex < sectionSize; putSectionIndex++) {

      PutSectionContext putSectionContext = putRecipeContext.sectionContexts().get(putSectionIndex);
      PutMutation<PutSection, Section> putSectionMutation = putSectionContext.sectionMutation();

      if (!putSectionMutation.isNew()) {
        Section section = putSectionMutation.entity();
        section.setTitle(putSectionMutation.request().title());
        section.setDescription(putSectionMutation.request().description());
        section.setNumber(putSectionMutation.number());
      }

    }
    if (!sectionsToBeCreated.isEmpty()) {
      sectionRepository.saveAll(sectionsToBeCreated);
    }

    List<Step> stepsToBeCreated = new ArrayList<>();
    Map<Section, List<Step>> stepsBySection = new HashMap<>();
    for (int putSectionIndex = 0; putSectionIndex < sectionSize; putSectionIndex++) {

      PutSectionContext putSectionContext = putRecipeContext.sectionContexts().get(putSectionIndex);
      Section section = finalSectionsByIndex.get(putSectionIndex);
      Set<Long> stepIdsToUpdated = new HashSet<>();
      List<Step> sectionSteps = new ArrayList<>();

      for (PutStepContext putStepContext : putSectionContext.stepContexts()) {
        if (!putStepContext.stepMutation().isNew()) {
          stepIdsToUpdated.add(putStepContext.stepMutation().entity().getId());
        }
      }

      if (!putSectionContext.sectionMutation().isNew()) {

        List<Step> stepsToBeDeleted = stepRepository.findAllBySectionId(section.getId())
            .stream()
            .filter(step -> !stepIdsToUpdated.contains(step.getId()))
            .toList();

        if (!stepsToBeDeleted.isEmpty()) {
          mediaS3KeysToBeDeleted.addAll(stepsToBeDeleted.stream()
              .map(Step::getMediaContentS3Key)
              .filter(StringUtils::hasText)
              .toList());
          stepRepository.deleteAllById(stepsToBeDeleted.stream().map(Step::getId).toList());
        }

      }

      int stepSize = putSectionContext.stepContexts().size();
      for (int stepIndex = 0; stepIndex < stepSize; stepIndex++) {
        var stepContext = putSectionContext.stepContexts().get(stepIndex);
        if (!stepContext.stepMutation().isNew()) {
          Step step = stepContext.stepMutation().entity();
          step.setNumber(-stepSize + stepIndex);
        }
      }
      stepRepository.flush();

      for (int stepIndex = 0; stepIndex < stepSize; stepIndex++) {

        PutStepContext stepContext = putSectionContext.stepContexts().get(stepIndex);
        PutMutation<PutStep, Step> stepMutation = stepContext.stepMutation();

        if (stepMutation.isNew()) {
          Step step = Step.builder()
              .section(section)
              .title(stepMutation.request().title())
              .description(stepMutation.request().description())
              .number(stepMutation.number())
              .build();
          stepsToBeCreated.add(step);
          sectionSteps.add(step);
        } else {
          Step step = stepMutation.entity();
          step.setTitle(stepMutation.request().title());
          step.setDescription(stepMutation.request().description());
          step.setNumber(stepMutation.number());
          sectionSteps.add(step);
        }
      }

      stepsBySection.put(section, sectionSteps);

    }

    if (!stepsToBeCreated.isEmpty()) {
      stepRepository.saveAll(stepsToBeCreated);
    }

    if (!mediaS3KeysToBeDeleted.isEmpty()) {
      mediaService.deleteMedia(mediaS3KeysToBeDeleted);
    }

    return recipeMapper.toRecipeDetailsResponse(
        recipeRepository.save(recipe),
        stepsBySection
    );

  }

  @Override
  @Transactional
  public RecipeSummaryResponse updateAccessLevelByRecipe(
      Recipe recipe,
      PatchRecipeAccessLevelRequest screenedRequest
  ) {

    boolean mutated = false;

    if (screenedRequest.accessLevelOperation() == PatchRequestOperation.UPDATE
        && screenedRequest.accessLevel() != recipe.getAccessLevel()
    ) {
      if (screenedRequest.accessLevel() == RecipeAccessLevel.PUBLIC) {
        beneficiaryRepository.deleteAll(recipe.getBeneficiaries());
      }
      recipe.setAccessLevel(screenedRequest.accessLevel());
      mutated = true;
    }

    return recipeMapper.toRecipeSummaryResponse(
        mutated
            ? recipeRepository.save(recipe)
            : recipe
    );

  }

  @Override
  @Transactional
  public void deleteAllByRecipes(
      List<Recipe> recipes,
      UserPrincipal principal
  ) {

    List<String> mediaS3KeysToBeDeleted = new ArrayList<>();

    recipes.forEach(recipe -> {

      if (StringUtils.hasText(recipe.getMediaContentS3Key())) {
        mediaS3KeysToBeDeleted.add(recipe.getMediaContentS3Key());
      }

      recipe.getSections().forEach(section -> {

        if (StringUtils.hasText(section.getMediaContentS3Key())) {
          mediaS3KeysToBeDeleted.add(section.getMediaContentS3Key());
        }

        section.getSteps().forEach(step -> {
          if (StringUtils.hasText(step.getMediaContentS3Key())) {
            mediaS3KeysToBeDeleted.add(step.getMediaContentS3Key());
          }
        });

      });

    });

    recipeRepository.deleteAll(recipes);

    if (!mediaS3KeysToBeDeleted.isEmpty()) {
      mediaService.deleteMedia(mediaS3KeysToBeDeleted);
    }

  }

  @Override
  @Transactional
  public MediaResponse updateMediaByRecipe(
      Recipe recipe,
      MultipartFile media,
      UserPrincipal principal
  ) {

    String mediaS3Key = String.format(AwsS3Configs.RECIPE_KEY_NAME_TEMPLATE, recipe.getId());
    recipe.setMediaContentS3Key(mediaS3Key);
    recipe.setMediaContentName(media.getOriginalFilename());
    recipe.setMediaContentType(media.getContentType());
    recipe.setLastUpdatedAt(Instant.now());
    recipeRepository.save(recipe);

    mediaService.updateMedia(mediaS3Key, media);

    return commonMapper.toMediaResponse(
        recipe.getMediaContentS3Key(),
        recipe.getMediaContentName(),
        recipe.getMediaContentType()
    );

  }

  @Override
  @Transactional
  public void deleteMediaByRecipe(
      Recipe recipe,
      UserPrincipal principal
  ) {

    String s3KeyToBeDeleted = recipe.getMediaContentS3Key();
    recipe.setMediaContentS3Key(null);
    recipe.setMediaContentName(null);
    recipe.setMediaContentType(null);
    recipe.setLastUpdatedAt(Instant.now());
    recipeRepository.save(recipe);

    mediaService.deleteMedia(List.of(s3KeyToBeDeleted));

  }

}
