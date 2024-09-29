package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private LocalDateTime localDateTime;

    private BookingDto bookingDto;

    private ResponseBookingDto responseBookingDto;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.now();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(localDateTime)
                .end(localDateTime.plusHours(2))
                .itemId(1)
                .build();
        responseBookingDto = ResponseBookingDto.builder()
                .id(1)
                .start(localDateTime)
                .end(localDateTime.plusHours(2))
                .status(Status.APPROVED)
                .build();
    }

    @SneakyThrows
    @Test
    void createTest() {
        when(bookingService.create(bookingDto, 1L))
                .thenReturn(responseBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).create(bookingDto, 1L);
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void updateTest() {
        when(bookingService.update(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(responseBookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).update(anyLong(), anyBoolean(), anyLong());
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).findById(anyLong(), anyLong());
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void getAllByBookerTest() {
        List<ResponseBookingDto> listBookings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseBookingDto responseBookingDto = ResponseBookingDto.builder()
                    .id(i)
                    .start(localDateTime)
                    .end(localDateTime.plusHours(1))
                    .status(Status.APPROVED)
                    .build();
            listBookings.add(responseBookingDto);
        }

        when(bookingService.getAllByBooker(1L, State.ALL))
                .thenReturn(listBookings);

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
        verify(bookingService, times(1)).getAllByBooker(1L, State.ALL);
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest() {
        List<ResponseBookingDto> listBookings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseBookingDto responseBookingDto = ResponseBookingDto.builder()
                    .id(i)
                    .start(localDateTime)
                    .end(localDateTime.plusHours(1))
                    .status(Status.APPROVED)
                    .build();
            listBookings.add(responseBookingDto);
        }

        when(bookingService.getAllByOwner(1L, State.ALL))
                .thenReturn(listBookings);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
        verify(bookingService, times(1)).getAllByOwner(1L, State.ALL);
        verifyNoMoreInteractions(bookingService);
    }
}