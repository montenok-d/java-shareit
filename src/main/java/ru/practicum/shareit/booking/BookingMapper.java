package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public final class BookingMapper {

    public static BookingDto toBookingDto (Booking booking) {
        BookingDto dto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
        return dto;
    }
}
