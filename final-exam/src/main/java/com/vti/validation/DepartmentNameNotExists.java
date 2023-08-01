package com.vti.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Constraint(
            validatedBy = DepartmentNameNotExistsValidator.class
    )
    public @interface DepartmentNameNotExists {

        // in message
        String message() default "{DepartmentForm.name.NotExists}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
