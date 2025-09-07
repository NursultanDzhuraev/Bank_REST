package com.example.bankcards.validation.password;

import ebook.dto.request.UserSignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSignUpRequest> {
    @Override
    public boolean isValid(UserSignUpRequest request, ConstraintValidatorContext context) {
        return request.password().equals(request.confirmPassword());
    }
}