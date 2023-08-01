package com.vti.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Constraint(
            validatedBy = AccountUserNameNotExistsValidator.class
    )
    public @interface AccountUserNameNotExists {

        // in message
        String message() default "Tài khoản đã tồn tại";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
