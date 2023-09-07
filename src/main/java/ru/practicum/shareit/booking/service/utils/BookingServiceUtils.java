package ru.practicum.shareit.booking.service.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BookingServiceUtils {
    private final ItemService itemService;
    private final UserService userService;
    public Booking checkAndConvertToBooking(long userId, BookingDto bookingDto, LocalDateTime rentalEnd) {
        User booker = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (!item.isAvailable()) {
            throw new ValidationException("Item is not available for this rental time.");
        }
        Booking booking = BookingMapper.convertToBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);

        checkStartAndEndTimes(booking);
        return booking;
    }

    public BookingDto convertToDto(Booking booking) {
        return BookingMapper.convertToDto(booking);
    }

    public void checkIsUserOwner(long userId, Booking booking) {
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("User with id " + userId + " is not owner of this item.");
        }
    }

    public void checkIsUserBookerOrOwner(long userId, Booking booking) {
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("User with id " + userId + " is not booker or owner of this item.");
        }
    }

    public void setNotAvailableToItem(long itemId) {
        itemService.setItemIsAvailable(itemId, false);
    }

    public void checkStartAndEndTimes(Booking booking) {
        if (booking.getStart() == null || booking.getEnd() == null) {
            throw new ValidationException("Incorrect rental time information.");
        }

        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Rental start time in past.");
        }
        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Rental end time in past.");
        }
        if (end.isEqual(start)) {
            throw new ValidationException("Rental timelines equals.");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Rental end time is before rental start time.");
        }
    }
}
