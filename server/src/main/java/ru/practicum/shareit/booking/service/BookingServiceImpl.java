package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseBookingDto create(BookingDto bookingDto, long userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", userId)));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", bookingDto.getItemId())));
        if (!item.getAvailable()) {
            throw new ValidationException("Booking is not available");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        ResponseBookingDto dto = BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        log.info("DTO: {}", dto);
        return dto;
    }

    @Transactional
    @Override
    public ResponseBookingDto update(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Booking № %d not found", bookingId)));
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(String.format("User № %d not found", userId)));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Access denied");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public ResponseBookingDto findById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Booking № %d not found", bookingId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", userId)));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ValidationException("Booking is not available");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getAllByBooker(long userId, State state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", userId)));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL -> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case CURRENT ->
                    bookings = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            case PAST ->
                    bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case FUTURE ->
                    bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            case WAITING ->
                    bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        }
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<ResponseBookingDto> getAllByOwner(long userId, State state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", userId)));
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL -> bookings = bookingRepository.findByOwnerId(userId);
            case CURRENT -> bookings = bookingRepository.findByOwnerCurrentBookings(userId);
            case PAST -> bookings = bookingRepository.findByOwnerPastBookings(userId);
            case FUTURE -> bookings = bookingRepository.findByOwnerFutureBookings(userId);
            case WAITING -> bookings = bookingRepository.findByOwnerAndStatus(userId, Status.WAITING);
            case REJECTED -> bookings = bookingRepository.findByOwnerAndStatus(userId, Status.REJECTED);
        }
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }
}
