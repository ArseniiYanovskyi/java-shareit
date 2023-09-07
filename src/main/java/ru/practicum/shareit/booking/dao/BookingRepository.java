package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // createBooking ---------------
    @Query("SELECT Booking.end as end " +
            "FROM Booking " +
            "WHERE Booking.item.id = ?1 " +
            "ORDER BY end DESC " +
            "LIMIT 1")
    LocalDateTime findRentalEndForBookingId(long bookingId);

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
    @Query("SELECT Booking " +
            "FROM Booking " +
            "WHERE Booking.item.id = ?1 AND Booking.end < ?2 " +
            "ORDER BY end DESC " +
            "LIMIT 1")
    @Nullable
    Booking findLastBooking(long itemId, LocalDateTime now);

    @Query("SELECT Booking " +
            "FROM Booking " +
            "WHERE Booking.item.id = ?1 AND Booking.start > ?2 " +
            "ORDER BY end ASC " +
            "LIMIT 1")
    @Nullable
    Booking findNextBooking(long itemId, LocalDateTime now);
}
