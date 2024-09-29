package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    protected void testBookingDto() throws Exception {

        User user = User.builder()
                .id(1L)
                .name("Name")
                .email("mail2@mail.ru")
                .build();

        User owner = User.builder()
                .id(2L)
                .name("Name")
                .email("mail3@mail.ru")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .booker(user.getId())
                .itemId(item.getId())
                .start(LocalDateTime.of(2024, 10, 1, 15, 30))
                .end(LocalDateTime.of(2024, 10, 30, 15, 30))
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.booker").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-10-01T15:30:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-10-30T15:30:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}