package com.vti.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = DepartmentIdExistsValidator.class
)
public @interface DepartmentIdExists {

    // in message
    String message() default "Phòng ban chưa tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
