package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.errors.exceptions.ConflictException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.PostBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.errors.BeneficiaryErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.BeneficiaryMapper;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;
import com.dvaults.recipecatalogue.modules.core.repositories.RecipeRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.BeneficiaryService;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.BeneficiaryAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryAuthorizationProxyServiceImpl implements BeneficiaryAuthorizationProxyService {

  private final BeneficiaryService beneficiaryService;

  private final RecipeRepository recipeRepository;
  private final UserRepository userRepository;

  private final BeneficiaryMapper beneficiaryMapper;

  @Override
  public List<BeneficiaryResponse> findAllByRecipeId(
      UserPrincipal principal,
      long recipeId
  ) {

    Recipe recipe = recipeRepository.findByIdFetchBeneficiaries(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001));

    if (recipe.getAccessLevel() == RecipeAccessLevel.PRIVATE) {
      return List.of();
    }

    return beneficiaryService.findAllByRecipe(recipe);

  }

  @Override
  public List<BeneficiaryResponse> createByRecipeId(
      UserPrincipal principal,
      long recipeId,
      PostBeneficiaryRequest postBeneficiaryRequest
  ) {

    Recipe recipe = recipeRepository.findByIdFetchBeneficiaries(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001));

    if (recipe.getAccessLevel() == RecipeAccessLevel.PUBLIC) {
      throw new ConflictException(BeneficiaryErrorDictionary.INVALID_BENEFICIARY_DETAILS_003);
    }

    return beneficiaryService.createByRecipeAndUsers(
        recipe,
        userRepository.findAllByIdIn(
            beneficiaryMapper.screenPostBeneficiaryRequest(postBeneficiaryRequest)
                .userIds())
    );

  }

  @Override
  public void deleteAllByRecipeId(
      UserPrincipal principal,
      long recipeId,
      DeleteBeneficiaryRequest deleteBeneficiaryRequest
  ) {

    Recipe recipe = recipeRepository.findByIdFetchBeneficiaries(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException(RecipeErrorDictionary.RECIPE_NOT_FOUND_001));

    if (recipe.getAccessLevel() == RecipeAccessLevel.PUBLIC) {
      throw new ConflictException(BeneficiaryErrorDictionary.INVALID_BENEFICIARY_DETAILS_004);
    }

    beneficiaryService.deleteAllByRecipe(
        recipe,
        beneficiaryMapper.screenDeleteBeneficiaryRequest(deleteBeneficiaryRequest)
    );

  }

}
