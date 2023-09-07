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
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto createBooking(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.debug("Received request to create new booking from user {}.", userId);

        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeItemStatus(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                       @PathVariable(value = "bookingId") long bookingId,
                                       @RequestParam(required = true) boolean approved) {
        log.debug("Received request from user {} to change status to {} in booking {}.", userId, approved, bookingId);

        return bookingService.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getInfoAboutBooking(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                          @PathVariable(value = "bookingId") long bookingId) {
        log.debug("Received request to get info about booking {} from user {}.", userId, bookingId);

        return bookingService.getBookingInfo(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getUsersBookings(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Received request to get bookings of user {}.", userId);

        return bookingService.getUsersBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUsersItemsBookings(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Received request to get user {} items bookings.", userId);

        return bookingService.getUsersItemsBookings(userId, state);
    }
}
