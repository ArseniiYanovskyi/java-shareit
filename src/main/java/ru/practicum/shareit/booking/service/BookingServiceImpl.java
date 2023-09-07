package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.utils.BookingServiceUtils;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.UnknownStateException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingServiceUtils utils;

    @Override
    @Transactional
    public BookingDto createBooking(long userId, BookingDto bookingDto) {
        LocalDateTime rentalEnd = bookingRepository.findRentalEndForBookingId(bookingDto.getItemId());
        Booking booking = utils.checkAndConvertToBooking(userId, bookingDto, rentalEnd);
        booking.setStatus(Status.WAITING);

        log.debug("Sending to DAO request to create new booking information.");
        return utils.convertToDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto setStatus(long userId, long bookingId, boolean isApproved) {
        Booking booking = getBookingById(bookingId);
        utils.checkIsUserOwner(userId, booking);
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
            utils.setNotAvailableToItem(booking.getItem().getId());
        }
        if (!isApproved) {
            booking.setStatus(Status.REJECTED);
        }
        log.debug("Sending to DAO request to update booking {} information.", bookingId);
        return utils.convertToDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto getBookingInfo(long userId, long bookingId) {
        Booking booking = getBookingById(bookingId);
        utils.checkIsUserBookerOrOwner(userId, booking);
        log.debug("Sending to DAO request to get booking {}.", bookingId);
        return utils.convertToDto(booking);
    }

    @Override
    @Transactional
    public List<BookingDto> getUsersBookings(long userId, String state) {
        log.debug("Sending to DAO request to get user {} bookings.", userId);
        List<Booking> resultList;
        switch (state) {
            case "ALL":
                resultList = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                resultList =  bookingRepository.findAllByStartBeforeAndEndIsAfterAndBooker_IdIsOrderByStartDesc
                                (LocalDateTime.now(), LocalDateTime.now(), userId);
                break;
            case "PAST":
                resultList =  bookingRepository.findAllByEndBeforeAndBooker_IdIsOrderByStartDesc
                                (LocalDateTime.now(), userId);
                break;
            case "FUTURE":
                resultList =  bookingRepository.findAllByStartIsAfterAndBooker_IdIsOrderByStartDesc
                                (LocalDateTime.now(), userId);
                break;
            case "WAITING":
                resultList =  bookingRepository.findAllByStatusAndBooker_IdIsOrderByStartDesc
                                (Status.WAITING, userId);
                break;
            case "REJECTED":
                resultList =  bookingRepository.findAllByStatusAndBooker_IdIsOrderByStartDesc
                                (Status.REJECTED, userId);
                break;
            default:
                throw new UnknownStateException("Unknown state: " + state);
        }
        return resultList.stream()
                .map(BookingMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookingDto> getUsersItemsBookings(long userId, String state) {
        log.debug("Sending to DAO request to get user's {} items bookings.", userId);
        List<Booking> resultList;
        switch (state) {
            case "ALL":
                resultList =  bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                resultList =  bookingRepository.findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc
                                (LocalDateTime.now(), LocalDateTime.now(), userId);
                break;
            case "PAST":
                resultList =  bookingRepository.findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc
                                (LocalDateTime.now(), userId);
                break;
            case "FUTURE":
                resultList =  bookingRepository.findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc
                                (LocalDateTime.now(), userId);
                break;
            case "WAITING":
                resultList =  bookingRepository.findAllByStatusAndItem_Owner_IdIsOrderByStartDesc
                                (Status.WAITING, userId);
                break;
            case "REJECTED":
                resultList =  bookingRepository.findAllByStatusAndItem_Owner_IdIsOrderByStartDesc
                                (Status.REJECTED, userId);
                break;
            default:
                throw new UnknownStateException("Unknown state: " + state);
        }
        return resultList.stream()
                .map(BookingMapper::convertToDto)
                .collect(Collectors.toList());
    }

    private Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not present."));
    }
}
