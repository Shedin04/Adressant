package com.shedin.adressant.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Data integrity violation")
    @ExceptionHandler(ConstraintViolationException.class)
    public void error() {
        log.warn("Invalid email was entered");
    }
}
