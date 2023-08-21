package ru.practicum.shareit.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends ResponseStatusException {
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
