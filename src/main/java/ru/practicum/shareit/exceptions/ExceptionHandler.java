package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.model.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {
    private final String NOT_FOUND = HttpStatus.NOT_FOUND.toString();
    private final String BAD_REQUEST = HttpStatus.BAD_REQUEST.toString();
    private final String CONFLICT = HttpStatus.CONFLICT.toString();

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse errorResponse(NotFoundException e) {
        log.debug("Returning {} answer with message: {}", NOT_FOUND, e.getMessage());
        return new ErrorResponse(NOT_FOUND, e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorResponse(ValidationException e) {
        log.debug("Returning {} answer with message: {}", BAD_REQUEST, e.getMessage());
        return new ErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse errorResponse(AlreadyUsedException e) {
        log.debug("Returning {} answer with message: {}", CONFLICT, e.getMessage());
        return new ErrorResponse(CONFLICT, e.getMessage());
    }
}
