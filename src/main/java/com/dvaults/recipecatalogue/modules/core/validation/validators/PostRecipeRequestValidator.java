package com.dvaults.recipecatalogue.modules.core.validation.validators;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.ValidPostRecipeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PostRecipeRequestValidator implements ConstraintValidator<ValidPostRecipeRequest, PostRecipeRequest> {

  private final RecipeMapper recipeMapper;

  @Override
  public boolean isValid(
      PostRecipeRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PostRecipeRequest screenedRequest = recipeMapper.screenPostRecipeRequest(request);

    if (!StringUtils.hasText(screenedRequest.name())) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "name",
          RecipeErrorDictionary.NAME_ERROR_MESSAGE_001
      );
    }

    if (!StringUtils.hasText(screenedRequest.description())) {
      isValid = false;
      ValidationUtils.constructErrorFromMessage(
          errors,
          "description",
          RecipeErrorDictionary.DESCRIPTION_ERROR_MESSAGE_001
      );
    }

    if (!isValid) {
      throw new RequestValidationException(
          RecipeErrorDictionary.INVALID_RECIPE_DETAILS_001,
          errors
      );
    }

    return isValid;

  }

}
