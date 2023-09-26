package ru.practicum.shareit.exception;

import org.junit.jupiter.api.*;
import ru.practicum.shareit.exceptions.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExceptionTests {

    @Test
    @Order(value = 1)
    @DisplayName("1 - AlreadyUsedException.")
    void shouldTrowAlreadyUsedException() {
        String message = "AlreadyUsedException";
        Exception exception = assertThrows(AlreadyUsedException.class,
                () -> {
                    throw new AlreadyUsedException(message);
                });
        assertThat("409 CONFLICT \"AlreadyUsedException\"", equalTo(exception.getMessage()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - DBRequestException.")
    void shouldTrowDBRequestException() {
        String message = "DBRequestException";
        Exception exception = assertThrows(DBRequestException.class,
                () -> {
                    throw new DBRequestException(message);
                });
        assertThat(message, equalTo(exception.getMessage()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - NotFoundException.")
    void shouldTrowNotFoundException() {
        String message = "NotFoundException";
        Exception exception = assertThrows(NotFoundException.class,
                () -> {
                    throw new NotFoundException(message);
                });
        assertThat("404 NOT_FOUND \"NotFoundException\"", equalTo(exception.getMessage()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - UnknownStateException.")
    void shouldTrowUnknownStateException() {
        String message = "UnknownStateException";
        Exception exception = assertThrows(UnknownStateException.class,
                () -> {
                    throw new UnknownStateException(message);
                });
        assertThat(message, equalTo(exception.getMessage()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - ValidationException.")
    void shouldTrowValidationException() {
        String message = "ValidationException";
        Exception exception = assertThrows(ValidationException.class,
                () -> {
                    throw new ValidationException(message);
                });
        assertThat("400 BAD_REQUEST \"ValidationException\"", equalTo(exception.getMessage()));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - ErrorResponse.")
    void shouldErrorResponse() {
        String error = "404_NOT_FOUND";
        String message = "something not found.";
        ErrorResponse errorResponse = new ErrorResponse(error, message);
        assertThat(errorResponse.getError(), equalTo(error));
        assertThat(errorResponse.getMessage(), equalTo(message));
    }

}
