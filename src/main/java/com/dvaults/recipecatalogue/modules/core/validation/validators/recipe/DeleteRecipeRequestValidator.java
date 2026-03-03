package com.dvaults.recipecatalogue.modules.core.validation.validators.recipe;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.DeleteRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.recipe.ValidDeleteRecipeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteRecipeRequestValidator implements ConstraintValidator<ValidDeleteRecipeRequest, DeleteRecipeRequest> {

  private final RecipeMapper recipeMapper;

  @Override
  public boolean isValid(
      DeleteRecipeRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    DeleteRecipeRequest screenedRequest = recipeMapper.screenDeleteRecipeRequest(request);

    if (screenedRequest.recipeIds()
        .stream()
        .anyMatch(recipeId -> recipeId != null && recipeId <= 0)
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "recipeIds",
          RecipeErrorDictionary.RECIPE_IDS_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          RecipeErrorDictionary.INVALID_RECIPE_DETAILS_004,
          errors
      );
    }

    return isValid;

  }

}