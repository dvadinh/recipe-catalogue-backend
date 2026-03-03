package com.dvaults.recipecatalogue.modules.core.validation.validators.recipe;

import com.dvaults.recipecatalogue.common.dtos.requests.PatchRequestOperation;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.recipe.ValidPatchRecipeAccessLevelRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PatchRecipeAccessLevelRequestValidator implements ConstraintValidator<ValidPatchRecipeAccessLevelRequest, PatchRecipeAccessLevelRequest> {

  private final RecipeMapper recipeMapper;

  @Override
  public boolean isValid(
      PatchRecipeAccessLevelRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PatchRecipeAccessLevelRequest screenedRequest = recipeMapper.screenPatchRecipeAccessLevelRequest(request);

    if (screenedRequest.accessLevelOperation() == PatchRequestOperation.CLEAR) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "accessLevelOperation",
          RecipeErrorDictionary.ACCESS_LEVEL_OPERATION_ERROR_MESSAGE_001
      );
    }

    if (screenedRequest.accessLevelOperation() == PatchRequestOperation.UPDATE
        && request.accessLevel() == null
    ) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "accessLevel",
          RecipeErrorDictionary.ACCESS_LEVEL_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          RecipeErrorDictionary.INVALID_RECIPE_DETAILS_003,
          errors
      );
    }

    return isValid;

  }

}