package com.bilin.controller.exception;

import com.bilin.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result doException(Exception ex) {
        log.error("An exception occurred", ex);
        return Result.error("An error has occurred. Please contact administrator.");
    }
}
