package src.main.java.ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum RequestState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static Optional<RequestState> from(String stringState) {
        for (RequestState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
