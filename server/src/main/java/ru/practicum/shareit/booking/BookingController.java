package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final String httpHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto createBooking(@RequestHeader(httpHeaderUserId) long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.debug("Received request to create new booking from user {}.", userId);

        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeItemStatus(@RequestHeader(httpHeaderUserId) long userId,
                                       @PathVariable(value = "bookingId") long bookingId,
                                       @RequestParam(required = true) boolean approved) {
        log.debug("Received request from user {} to change status to {} in booking {}.", userId, approved, bookingId);

        return bookingService.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getInfoAboutBooking(@RequestHeader(httpHeaderUserId) long userId,
                                          @PathVariable(value = "bookingId") long bookingId) {
        log.debug("Received request to get info about booking {} from user {}.", userId, bookingId);

        return bookingService.getBookingInfo(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getUsersBookings(@RequestHeader(httpHeaderUserId) long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(value = "from", required = false) Integer from,
                                             @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get bookings of user {}.", userId);
        if (from != null && size != null) {
            return bookingService.getUsersBookingsPagination(userId, state, from, size);
        }
        return bookingService.getUsersBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUsersItemsBookings(@RequestHeader(httpHeaderUserId) long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", required = false) Integer from,
                                                  @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get user {} items bookings.", userId);
        if (from != null && size != null) {
            return bookingService.getUsersItemsBookingsPagination(userId, state, from, size);
        }
        return bookingService.getUsersItemsBookings(userId, state);
    }
}
