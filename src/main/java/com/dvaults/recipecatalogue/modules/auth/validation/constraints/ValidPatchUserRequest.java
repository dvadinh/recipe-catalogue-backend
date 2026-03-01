package com.dvaults.recipecatalogue.modules.auth.validation.constraints;

import com.dvaults.recipecatalogue.modules.auth.validation.validators.PatchUserRequestValidator;
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
@Constraint(validatedBy = PatchUserRequestValidator.class)
public @interface ValidPatchUserRequest {

  String message() default "Invalid user update request body";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
