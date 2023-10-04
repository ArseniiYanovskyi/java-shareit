package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                @RequestBody BookingDto bookingDto) {
        log.debug("Received request to create new booking from user {}.", userId);

        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeItemStatus(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                   @PathVariable(value = "bookingId") long bookingId,
                                                   @RequestParam(required = true) boolean approved) {
        log.debug("Received request from user {} to change status to {} in booking {}.", userId, approved, bookingId);

        return bookingClient.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getInfoAboutBooking(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                      @PathVariable(value = "bookingId") long bookingId) {
        log.debug("Received request to get info about booking {} from user {}.", userId, bookingId);

        return bookingClient.getBookingInfo(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUsersBookings(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", required = false) Integer from,
                                                   @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get bookings of user {}.", userId);
        if (from != null && size != null) {
            return bookingClient.getUsersBookingsPagination(userId, state, from, size);
        }
        return bookingClient.getUsersBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUsersItemsBookings(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(value = "from", required = false) Integer from,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get user {} items bookings.", userId);
        if (from != null && size != null) {
            return bookingClient.getUsersItemsBookingsPagination(userId, state, from, size);
        }
        return bookingClient.getUsersItemsBookings(userId, state);
    }
}