package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, BookingDto bookingDto);
    BookingDto setStatus(long userId, long bookingId, boolean isApproved);
    BookingDto getBookingInfo(long userId, long bookingId);
    List<BookingDto> getUsersBookings(long userId, String state);
    List<BookingDto> getUsersItemsBookings(long userId, String state);
}
