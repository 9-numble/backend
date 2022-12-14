package com.project.numble.application.common.advice;

import com.project.numble.application.user.repository.exception.UserNotFoundException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> bindException(BindException e) {
        log.info("{}", e.getBindingResult().getFieldError());
        return utils.getFailureResponse(ExceptionType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, String> exceptionHandler(Exception e) {
        log.info("{}", e);
        return utils.getFailureResponse(ExceptionType.EXCEPTION);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> userNotFoundExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.USER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        log.info("{}", e);
        return utils.getFailureResponse(ExceptionType.HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> nullPointerExceptionHandler(NullPointerException e) {
        log.info("{}", e);
        return null;
    }
}
