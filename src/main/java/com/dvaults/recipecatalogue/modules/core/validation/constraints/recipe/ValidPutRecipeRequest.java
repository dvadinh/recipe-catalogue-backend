package com.dvaults.recipecatalogue.modules.core.validation.constraints.recipe;

import com.dvaults.recipecatalogue.modules.core.validation.validators.recipe.PutRecipeRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PutRecipeRequestValidator.class)
public @interface ValidPutRecipeRequest {

  String message() default "Invalid recipe update request body";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
