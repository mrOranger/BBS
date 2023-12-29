package com.edoardo.bbs.validation.rules;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
@Constraint(validatedBy = CustomerValidator.class)
public @interface BirthDateValid {

    String message () default "Customer must be adult.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
