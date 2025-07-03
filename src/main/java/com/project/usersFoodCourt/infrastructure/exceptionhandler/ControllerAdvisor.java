package com.project.usersFoodCourt.infrastructure.exceptionhandler;

import com.project.usersFoodCourt.domain.exception.BaseException;
import com.project.usersFoodCourt.domain.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.project.usersFoodCourt.utils.ErrorCatalog.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BaseException.class)
    public ErrorResponse handleBaseException(BaseException exception) {
        return ErrorResponse.builder()
                .code(exception.getErrorCatalog().getCode())
                .message(exception.getErrorCatalog().getMessage())
                .timestamp(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //MethodArgumentNotValidException validate the attributes of Dto
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();

        return ErrorResponse.builder()
                .code(INVALID_USER.getCode())
                .message(INVALID_USER.getMessage())
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .timestamp(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericError(Exception exception) {
        log.error("Unexpected error occurred", exception);
        return ErrorResponse.builder()
                .code(GENERIC_ERROR.getCode())
                .message(GENERIC_ERROR.getMessage())
                .details(Collections.singletonList(exception.getMessage()))
                .timestamp(LocalDate.now())
                .build();
    }
    
}