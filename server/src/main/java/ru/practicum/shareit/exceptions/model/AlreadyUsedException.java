package ru.practicum.shareit.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyUsedException extends ResponseStatusException {
    public AlreadyUsedException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}