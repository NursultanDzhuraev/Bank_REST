package com.example.bankcards.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotFoundException notFoundException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND).
                exceptionClassName(NotFoundException.class.getSimpleName()).
                message(notFoundException.getMessage())
                .build();
    }
}
