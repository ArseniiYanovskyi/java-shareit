package ru.practicum.shareit.exception.model;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String message) {
        super(message);
    }
}
