package ru.practicum.shareit.booking.service.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BookingServiceUtils {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Booking checkAndConvertToBooking(long userId, BookingDto bookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " does not present in repository."));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + bookingDto.getItemId() + " does not present in repository."));
        if (!item.getIsAvailable()) {
            throw new ValidationException("Item is not available for this rental time.");
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Owner can't book own item.");
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

    public void checkIfUserPresent(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " does not present in repository."));
    }

    public void checkIsUserOwner(long userId, Booking booking) {
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("User with id " + userId + " is not owner of this item.");
        }
    }

    public void checkIsUserBookerOrOwner(long userId, Booking booking) {
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("User with id " + userId + " is not booker or owner of this item.");
        }
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
        if (start.isEqual(end)) {
            throw new ValidationException("Rental timelines equals.");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Rental end time is before rental start time.");
        }
    }
}
