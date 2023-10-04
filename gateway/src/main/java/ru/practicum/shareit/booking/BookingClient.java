package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.model.UnknownStateException;
import src.main.java.ru.practicum.shareit.booking.dto.RequestState;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> setStatus(long userId, long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBookingInfo(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUsersBookings(long userId, String state) {
        RequestState requestState = RequestState.from(state)
                .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));
        return get("?state=" + requestState.name(), userId);
    }

    public ResponseEntity<Object> getUsersBookingsPagination(long userId, String state, Integer from, Integer size) {
        RequestState requestState = RequestState.from(state)
                .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));
        Map<String, Object> parameters = Map.of(
                "state", requestState.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> getUsersItemsBookings(long userId, String state) {
        RequestState requestState = RequestState.from(state)
                .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));
        return get("/owner?state=" + requestState.name(), userId);
    }

    public ResponseEntity<Object> getUsersItemsBookingsPagination(long userId, String state, Integer from, Integer size) {
        RequestState requestState = RequestState.from(state)
                .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));
        Map<String, Object> parameters = Map.of(
                "state", requestState.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}
