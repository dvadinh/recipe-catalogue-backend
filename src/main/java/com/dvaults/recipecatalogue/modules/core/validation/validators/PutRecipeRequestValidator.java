package com.dvaults.recipecatalogue.modules.core.validation.validators;

import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.utils.ValidationUtils;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.section.requests.PutSectionRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.step.requests.PutStepRequest;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.SectionErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.StepErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.mappers.RecipeMapper;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.ValidPostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.validation.constraints.ValidPutRecipeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PutRecipeRequestValidator implements ConstraintValidator<ValidPutRecipeRequest, PutRecipeRequest> {

  private final RecipeMapper recipeMapper;

  @Override
  public boolean isValid(
      PutRecipeRequest request,
      ConstraintValidatorContext constraintValidatorContext
  ) {

    boolean isValid = true;
    Map<String, String> errors = new HashMap<>();
    PutRecipeRequest screenedRequest = recipeMapper.screenPutRecipeRequest(request);

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

    for (PutSectionRequest putSectionRequest : screenedRequest.sections()) {

      if (!StringUtils.hasText(putSectionRequest.title())
          || !StringUtils.hasText(putSectionRequest.description())
      ) {
        isValid = false;
        if (!errors.containsKey("sections.title")) {
          ValidationUtils.constructErrorFromMessage(
              errors,
              "sections.title",
              SectionErrorDictionary.TITLE_ERROR_MESSAGE_001
          );
        }
        if (!errors.containsKey("sections.description")) {
          ValidationUtils.constructErrorFromMessage(
              errors,
              "sections.description",
              SectionErrorDictionary.DESCRIPTION_ERROR_MESSAGE_001
          );
        }
      }

      for (PutStepRequest putStepRequest : putSectionRequest.steps()) {
        if (!StringUtils.hasText(putStepRequest.title())
            || !StringUtils.hasText(putStepRequest.description())
        ) {
          isValid = false;
          if (!errors.containsKey("sections.steps.title")) {
            ValidationUtils.constructErrorFromMessage(
                errors,
                "sections.steps.title",
                StepErrorDictionary.TITLE_ERROR_MESSAGE_001
            );
          }
          if (!errors.containsKey("sections.steps.description")) {
            ValidationUtils.constructErrorFromMessage(
                errors,
                "sections.steps.description",
                StepErrorDictionary.DESCRIPTION_ERROR_MESSAGE_001
            );
          }
        }
      }

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
