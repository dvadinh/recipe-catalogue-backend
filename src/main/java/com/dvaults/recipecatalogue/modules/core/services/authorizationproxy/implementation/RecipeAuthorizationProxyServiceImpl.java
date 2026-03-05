package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.PutMutation;
import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.errors.exceptions.InternalServerErrorException;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.auth.errors.UserErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.PutRecipe;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.PutRecipeContext;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.DeleteRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeDetailsResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.section.PutSection;
import com.dvaults.recipecatalogue.modules.core.dtos.section.PutSectionContext;
import com.dvaults.recipecatalogue.modules.core.dtos.section.requests.PutSectionRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.step.PutStep;
import com.dvaults.recipecatalogue.modules.core.dtos.step.PutStepContext;
import com.dvaults.recipecatalogue.modules.core.dtos.step.requests.PutStepRequest;
import com.dvaults.recipecatalogue.modules.core.errors.MediaErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.SectionErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.StepErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.dvaults.recipecatalogue.modules.core.repositories.RecipeRepository;
import com.dvaults.recipecatalogue.modules.core.repositories.StepRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.RecipeService;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.RecipeAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RecipeAuthorizationProxyServiceImpl implements RecipeAuthorizationProxyService {

  private final RecipeService recipeService;

  private final RecipeRepository recipeRepository;
  private final StepRepository stepRepository;
  private final UserRepository userRepository;

  private final RecipeMapper recipeMapper;

  @Override
  public List<RecipeDetailsResponse> findAll(UserPrincipal principal) {
    return recipeService.findAllByUserId(principal.getId());
  }

  @Override
  public RecipeDetailsResponse findById(
      UserPrincipal principal,
      long id
  ) {
    return recipeService.findByRecipe(
        recipeRepository.findByIdFetchOwnerAndSectionsAndBeneficiaries(id)
            .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001))
    );
  }

  @Override
  public RecipeDetailsResponse create(
      UserPrincipal principal,
      PostRecipeRequest postRecipeRequest
  ) {
    return recipeService.create(
        recipeMapper.screenPostRecipeRequest(postRecipeRequest),
        userRepository.findById(principal.getId())
            .orElseThrow(() -> new InternalServerErrorException(UserErrorDictionary.USER_NOT_FOUND_002))
    );
  }

  @Override
  public RecipeDetailsResponse updateById(
      UserPrincipal principal,
      long id,
      PutRecipeRequest putRecipeRequest
  ) {

    Recipe recipe = recipeRepository.findByIdFetchOwnerAndSections(id)
        .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001));

    PutRecipeRequest screenedRequest = recipeMapper.screenPutRecipeRequest(putRecipeRequest);

    Map<Long, Section> existingSectionsById = recipe.getSections()
        .stream()
        .collect(Collectors.toMap(Section::getId, section -> section));

    Map<Long, Step> existingStepsById = stepRepository.findAllBySectionIdsIn(
            recipe.getSections()
                .stream()
                .map(Section::getId)
                .toList())
        .stream()
        .collect(Collectors.toMap(
            Step::getId,
            step -> step));

    List<PutSectionContext> putSectionContexts = IntStream.range(0, screenedRequest.sections().size())
        .mapToObj(putSectionIndex -> {

          PutSectionRequest putSectionRequest = screenedRequest.sections().get(putSectionIndex);

          Section existingSection = putSectionRequest.id() != null
              ? existingSectionsById.get(putSectionRequest.id())
              : null;

          if (putSectionRequest.id() != null && existingSection == null) {
            throw new ResourceNotFoundException(SectionErrorDictionary.SECTION_NOT_FOUND_001);
          }

          List<PutStepContext> putStepContexts = IntStream.range(0, putSectionRequest.steps().size())
              .mapToObj(putStepIndex -> {

                PutStepRequest putStepRequest = putSectionRequest.steps().get(putStepIndex);

                Step existingStep = putStepRequest.id() != null
                    ? existingStepsById.get(putStepRequest.id())
                    : null;

                if (putSectionRequest.id() != null && existingStep == null) {
                  throw new ResourceNotFoundException(StepErrorDictionary.STEP_NOT_FOUND_001);
                }

                return new PutStepContext(
                    new PutMutation<>(
                        new PutStep(putStepRequest),
                        existingStep,
                        putStepIndex + 1)
                );

              })
              .toList();

          return new PutSectionContext(
              new PutMutation<>(
                  new PutSection(putSectionRequest),
                  existingSection,
                  putSectionIndex + 1),
              putStepContexts
          );

        })
        .toList();

    return recipeService.updateByRecipeAndPutRecipeItems(
        recipe,
        new PutRecipeContext(
            new PutMutation<>(new PutRecipe(screenedRequest), recipe, null),
            putSectionContexts),
        principal
    );

  }

  @Override
  public RecipeSummaryResponse updateAccessLevelById(
      UserPrincipal principal,
      long id,
      PatchRecipeAccessLevelRequest patchRecipeAccessLevelRequest
  ) {
    return recipeService.updateAccessLevelByRecipe(
        recipeRepository.findByIdFetchBeneficiaries(id)
            .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001)),
        recipeMapper.screenPatchRecipeAccessLevelRequest(patchRecipeAccessLevelRequest)
    );
  }

  @Override
  public MediaResponse updateMediaById(
      UserPrincipal principal,
      long id,
      MultipartFile media
  ) {

    if (media == null || media.isEmpty()) {
      throw new RequestValidationException(MediaErrorDictionary.INVALID_MEDIA_DETAILS_001);
    }

    return recipeService.updateMediaByRecipe(
        recipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001)),
        media,
        principal
    );

  }

  @Override
  public void deleteAll(
      UserPrincipal principal,
      DeleteRecipeRequest deleteRecipeRequest
  ) {
    recipeService.deleteAllByRecipes(
        recipeRepository.findAllByIdsIn(deleteRecipeRequest.recipeIds()),
        principal
    );
  }

  @Override
  public void deleteMediaById(
      UserPrincipal principal,
      long id
  ) {
    recipeService.deleteMediaByRecipe(
        recipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001)),
        principal
    );
  }

}
