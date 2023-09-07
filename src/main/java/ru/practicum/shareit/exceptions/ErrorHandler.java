package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.model.*;

import java.sql.SQLDataException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse errorResponse(NotFoundException e) {
        log.debug("Returning {} answer with message: {}", NOT_FOUND, e.getMessage());
        return new ErrorResponse(NOT_FOUND.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse errorResponse(ValidationException e) {
        log.debug("Returning {} answer with message: {}", BAD_REQUEST, e.getMessage());
        return new ErrorResponse(BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse errorResponse(AlreadyUsedException e) {
        log.debug("Returning {} answer with message: {}", CONFLICT, e.getMessage());
        return new ErrorResponse(CONFLICT.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse errorResponse(Throwable e) {
        log.debug("Returning {} answer with message: {}", INTERNAL_SERVER_ERROR, e.getMessage());
        return new ErrorResponse(INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse errorResponse(SQLDataException e) {
        log.debug("Returning {} answer with message: {}", INTERNAL_SERVER_ERROR, e.getMessage());
        return new ErrorResponse(INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse errorResponse(DBRequestException e) {
        log.debug("Returning {} answer with message: {}", CONFLICT, e.getMessage());
        return new ErrorResponse(CONFLICT.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse errorResponse(javax.validation.ValidationException e) {
        log.debug("Returning {} answer with message: {}", BAD_REQUEST, e.getMessage());
        return new ErrorResponse(BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse errorResponse(UnknownStateException e) {
        log.debug("Returning {} answer with message: {}", INTERNAL_SERVER_ERROR, e.getMessage());
        return new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR.toString());
    }
}
