package com.edoardo.bbs.validation.rules;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerValidator implements ConstraintValidator<BirthDateValid, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return ChronoUnit.YEARS.between(value, LocalDate.now()) > 17;
    }
}
