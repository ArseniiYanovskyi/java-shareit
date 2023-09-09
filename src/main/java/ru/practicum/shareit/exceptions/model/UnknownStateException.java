package ru.practicum.shareit.exceptions.model;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String message) {
        super(message);
    }
}
