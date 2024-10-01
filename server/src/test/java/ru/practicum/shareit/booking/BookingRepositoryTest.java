package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private Item item;
    private Booking bookingCurrent;
    private Booking bookingFuture;
    private Booking bookingPast;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setName("Test User");
        booker.setEmail("testuser@example.com");
        userRepository.save(booker);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(booker);
        itemRepository.save(item);

        bookingCurrent = new Booking();
        bookingCurrent.setBooker(booker);
        bookingCurrent.setItem(item);
        bookingCurrent.setStart(LocalDateTime.now().minusDays(1));
        bookingCurrent.setEnd(LocalDateTime.now().plusDays(2));
        bookingCurrent.setStatus(Status.WAITING);
        bookingRepository.save(bookingCurrent);

        bookingFuture = new Booking();
        bookingFuture.setBooker(booker);
        bookingFuture.setItem(item);
        bookingFuture.setStart(LocalDateTime.now().plusDays(1));
        bookingFuture.setEnd(LocalDateTime.now().plusDays(2));
        bookingFuture.setStatus(Status.WAITING);
        bookingRepository.save(bookingFuture);

        bookingPast = new Booking();
        bookingPast.setBooker(booker);
        bookingPast.setItem(item);
        bookingPast.setStart(LocalDateTime.now().minusDays(3));
        bookingPast.setEnd(LocalDateTime.now().minusDays(2));
        bookingPast.setStatus(Status.WAITING);
        bookingRepository.save(bookingPast);
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindByBookerId() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId());
        Assertions.assertEquals(3, bookings.size());
    }

    @Test
    void testFindCurrentBookings() {
        List<Booking> currentBookings = bookingRepository
                .findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now(), LocalDateTime.now());

        Assertions.assertEquals(currentBookings.size(), 1);
        Assertions.assertTrue(currentBookings.contains(bookingCurrent));
    }

    @Test
    void testFindPastBookings() {
        List<Booking> pastBookings = bookingRepository
                .findByBookerIdAndEndIsBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        Assertions.assertTrue(pastBookings.contains(bookingPast));
        Assertions.assertFalse(pastBookings.contains(bookingFuture));
    }

    @Test
    void testFindFutureBookings() {
        List<Booking> futureBookings = bookingRepository
                .findByBookerIdAndStartIsAfterOrderByStartDesc(booker.getId(), LocalDateTime.now());
        Assertions.assertTrue(futureBookings.contains(bookingFuture));
        Assertions.assertFalse(futureBookings.contains(bookingPast));
    }

    @Test
    void testFindByOwnerId() {
        List<Booking> bookings = bookingRepository.findByOwnerId(booker.getId());
        Assertions.assertTrue(bookings.contains(bookingFuture));
        Assertions.assertTrue(bookings.contains(bookingPast));
    }

    @Test
    void testFindOwnerCurrentBookings() {
        List<Booking> currentBookings = bookingRepository.findByOwnerCurrentBookings(booker.getId());
        Assertions.assertTrue(currentBookings.contains(bookingCurrent));
    }

    @Test
    void testFindOwnerPastBookings() {
        List<Booking> pastBookings = bookingRepository.findByOwnerPastBookings(booker.getId());
        Assertions.assertTrue(pastBookings.contains(bookingPast));
        Assertions.assertFalse(pastBookings.contains(bookingFuture));
    }

    @Test
    void testFindOwnerFutureBookings() {
        List<Booking> futureBookings = bookingRepository.findByOwnerFutureBookings(booker.getId());
        Assertions.assertTrue(futureBookings.contains(bookingFuture));
        Assertions.assertFalse(futureBookings.contains(bookingPast));
    }

    @Test
    void testFindOwnerBookingsByStatusByUserId() {
        List<Booking> bookings = bookingRepository.findByOwnerAndStatus(booker.getId(), Status.WAITING);
        Assertions.assertTrue(bookings.contains(bookingFuture));
        Assertions.assertTrue(bookings.contains(bookingPast));
    }
}