package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody BookingDto bookingDto) {
        log.info("Create booking: {}, userId: {}", bookingDto, userId);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseBookingDto update(@PathVariable("id") long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam boolean approved) {
        log.info("Update booking. BookingId {}, userId: {}, approved: {}", bookingId, userId, approved);
        return bookingService.update(bookingId, approved, userId);
    }

    @GetMapping("/{id}")
    public ResponseBookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable("id") long bookingId) {
        log.info("Get booking. BookingId: {}, userId: {}", bookingId, userId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "ALL") State state) {
        log.info("Get bookings by booker. State: {}, userId: {}", state, userId);
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "ALL") State state) {
        log.info("Get bookings by owner. State: {}, userId: {}", state, userId);
        return bookingService.getAllByOwner(userId, state);
    }
}
