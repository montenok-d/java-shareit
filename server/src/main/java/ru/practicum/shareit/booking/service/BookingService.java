package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;


public interface BookingService {
    ResponseBookingDto create(BookingDto bookingDto, long userId);

    ResponseBookingDto update(long bookingId, boolean approved, long userId);

    ResponseBookingDto findById(long bookingId, long userId);

    List<ResponseBookingDto> getAllByBooker(long userId, State state);

    List<ResponseBookingDto> getAllByOwner(long userId, State state);
}
