package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.LastBookingDto;
import ru.practicum.shareit.booking.model.dto.NextBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {
    public static Booking convertToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (bookingDto.getId() != 0) {
            booking.setId(bookingDto.getId());
        }
        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }
        return booking;
    }

    public static BookingDto convertToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.convertToDto(booking.getBooker()))
                .item(ItemMapper.convertToDto(booking.getItem()))
                .status(booking.getStatus().toString())
                .build();
    }

    public static LastBookingDto convertToLastBookingDto(Booking booking) {
        return LastBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static NextBookingDto convertToNextBookingDto(Booking booking) {
        return NextBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
