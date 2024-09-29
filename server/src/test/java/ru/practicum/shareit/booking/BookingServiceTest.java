package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class BookingServiceTest {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Name")
                .email("mail2@mail.ru")
                .build();

        owner = User.builder()
                .name("Name")
                .email("mail3@mail.ru")
                .build();

        item = Item.builder()
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        userRepository.save(user);
        userRepository.save(owner);
        itemRepository.save(item);
    }

    @Test
    void createTest() {
        User newOwner = User.builder()
                .name("New user")
                .email("new@mail.ru")
                .build();

        Item newItem = Item.builder()
                .name("New item")
                .description("New description")
                .available(true)
                .owner(newOwner)
                .request(null)
                .build();
        userRepository.save(newOwner);
        itemRepository.save(newItem);

        BookingDto bookingCreateDto = BookingDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(3))
                .build();

        ResponseBookingDto createdBookingDto = bookingService.create(bookingCreateDto, user.getId());

        Assertions.assertThat(createdBookingDto.getBooker().getName()).isEqualTo(user.getName());
        Assertions.assertThat(createdBookingDto.getItem().getName()).isEqualTo(newItem.getName());
    }

    @Test
    void createNotAvailableTest() {
        User newOwner = User.builder()
                .name("New user")
                .email("new@mail.ru")
                .build();

        Item newItem = Item.builder()
                .name("New item")
                .description("New description")
                .available(false)
                .owner(newOwner)
                .request(null)
                .build();
        userRepository.save(newOwner);
        itemRepository.save(newItem);

        BookingDto bookingCreateDto = BookingDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(3))
                .build();

        ValidationException thrown = org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class, () ->
                bookingService.create(bookingCreateDto, user.getId()));
        assertEquals("Booking is not available", thrown.getMessage());
    }

    @Test
    void updateTest() {
        Booking booking = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.APPROVED)
                .build();

        booking = bookingRepository.save(booking);
        ResponseBookingDto bookingUpdateDto = bookingService.update(booking.getId(), true, owner.getId());

        Assertions.assertThat(bookingUpdateDto.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void getAllByBookerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.CURRENT);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking2.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking1.getId());
    }

    @Test
    void getAllByBookerPastTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(30))
                .end(LocalDateTime.now().minusDays(15))
                .status(Status.CANCELED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(14))
                .end(LocalDateTime.now().minusDays(7))
                .status(Status.CANCELED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.PAST);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking2.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking1.getId());
    }

    @Test
    void getAllByBookerFutureTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.FUTURE);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking2.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking1.getId());
    }

    @Test
    void getAllByBookerWaitingTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.WAITING);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
    }

    @Test
    void getAllByBookerRejectedTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.REJECTED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.REJECTED);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getAllByBooker() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(14))
                .end(LocalDateTime.now().minusDays(7))
                .status(Status.CANCELED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByBooker(user.getId(), State.ALL);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getAllByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(14))
                .end(LocalDateTime.now().minusDays(7))
                .status(Status.CANCELED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.ALL);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getCurrentByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.ALL);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getFutureByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.ALL);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getPastByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(30))
                .end(LocalDateTime.now().minusDays(15))
                .status(Status.CANCELED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusDays(14))
                .end(LocalDateTime.now().minusDays(7))
                .status(Status.CANCELED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.ALL);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
    }

    @Test
    void getRejectedByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.REJECTED)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.REJECTED);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
    }

    @Test
    void getWaitingByOwnerTest() {
        Booking booking1 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(30))
                .end(LocalDateTime.now().plusDays(45))
                .status(Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().plusDays(60))
                .end(LocalDateTime.now().plusDays(80))
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<ResponseBookingDto> bookings = bookingService.getAllByOwner(owner.getId(), State.WAITING);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
    }
}