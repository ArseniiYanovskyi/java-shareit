package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // getUsersBookingsPagination ----------
    Page<Booking> findAllByBooker_IdOrderByStartDesc(long bookerId, Pageable page);

    Page<Booking> findAllByStartBeforeAndEndIsAfterAndBooker_IdIsOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, long bookerId, Pageable page);

    Page<Booking> findAllByEndBeforeAndBooker_IdIsOrderByStartDesc
            (LocalDateTime end, long bookerId, Pageable page);

    Page<Booking> findAllByStartIsAfterAndBooker_IdIsOrderByStartDesc
            (LocalDateTime start, long bookerId, Pageable page);

    Page<Booking> findAllByStatusAndBooker_IdIsOrderByStartDesc
            (Status status, long bookerId, Pageable page);

    // getUsersItemsBookings ----------
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long ownerId);

    List<Booking> findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, long ownerId);

    List<Booking> findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc(LocalDateTime end, long ownerId);

    List<Booking> findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc(LocalDateTime start, long ownerId);

    List<Booking> findAllByStatusAndItem_Owner_IdIsOrderByStartDesc(Status status, long ownerId);

    // getUsersItemsBookingsPagination ----------

    Page<Booking> findAllByItem_Owner_IdOrderByStartDesc(long ownerId, Pageable page);

    Page<Booking> findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, long ownerId, Pageable page);

    Page<Booking> findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc
            (LocalDateTime end, long ownerId, Pageable page);

    Page<Booking> findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc
            (LocalDateTime start, long ownerId, Pageable page);

    Page<Booking> findAllByStatusAndItem_Owner_IdIsOrderByStartDesc
            (Status status, long ownerId, Pageable page);

    // last and next bookings by item id --------------

    Optional<Booking> findFirstByItem_IdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartIsAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime now, Status status);
}
