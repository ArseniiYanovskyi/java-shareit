package ru.practicum.shareit.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DBRequestException extends RuntimeException {
    public DBRequestException(String reason) {
        super(reason);
    }
}
