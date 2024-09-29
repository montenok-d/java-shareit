package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long bookerId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1")
    List<Booking> findByOwnerId(long ownerId);

    @Query("SELECT b FROM Booking AS b WHERE b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP AND b.item.owner.id = ?1")
    List<Booking> findByOwnerCurrentBookings(long userId);

    @Query("SELECT b FROM Booking AS b WHERE b.end < CURRENT_TIMESTAMP AND b.item.owner.id = ?1")
    List<Booking> findByOwnerPastBookings(long userId);

    @Query("SELECT b FROM Booking AS b WHERE b.start > CURRENT_TIMESTAMP AND b.item.owner.id = ?1")
    List<Booking> findByOwnerFutureBookings(long userId);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.status = ?2")
    List<Booking> findByOwnerAndStatus(long userId, Status status);

    @Query("SELECT b FROM Booking AS b WHERE b.item.id = ?1 AND b.end > ?2")
    Booking findLastBooking(long itemId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.item.id = ?1 AND b.start > ?2 AND b.status = APPROVED")
    Booking findNextBooking(long itemId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(long userId, long itemId, Status status, LocalDateTime date);
}
