package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // getUsersBookings ----------
    List<Booking> findAllByBooker_IdOrderByStartDesc(long bookerId);

    List<Booking> findAllByStartBeforeAndEndIsAfterAndBooker_IdIsOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, long bookerId);

    List<Booking> findAllByEndBeforeAndBooker_IdIsOrderByStartDesc(LocalDateTime end, long bookerId);

    List<Booking> findAllByStartIsAfterAndBooker_IdIsOrderByStartDesc(LocalDateTime start, long bookerId);

    List<Booking> findAllByStatusAndBooker_IdIsOrderByStartDesc(Status status, long bookerId);

    // getUsersItemsBookings ----------
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long ownerId);

    List<Booking> findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, long ownerId);

    List<Booking> findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc(LocalDateTime end, long ownerId);

    List<Booking> findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc(LocalDateTime start, long ownerId);

    List<Booking> findAllByStatusAndItem_Owner_IdIsOrderByStartDesc(Status status, long ownerId);

    // last and next bookings by item id --------------

    Optional<Booking> findFirstByItem_IdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartIsAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime now, Status status);
}
