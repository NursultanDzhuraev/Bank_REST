package com.example.bankcards.validation.password;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Пароль должен содержать минимум 8 символов, одну заглавную букву, одну цифру и спецсимвол";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}