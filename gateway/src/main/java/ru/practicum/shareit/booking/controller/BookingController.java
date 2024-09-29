package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RequestBookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create booking: {}, userId: {}", bookingDto, userId);
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") long bookingId,
                                         @RequestParam boolean approved,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Update booking. BookingId {}, userId: {}, approved: {}", bookingId, userId, approved);
        return bookingClient.update(bookingId, userId, approved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get booking. BookingId: {}, userId: {}", bookingId, userId);
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Get bookings by booker. State: {}, userId: {}", state, userId);
        return bookingClient.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Get bookings by owner. State: {}, userId: {}", state, userId);
        return bookingClient.getAllByOwner(userId, state);
    }
}