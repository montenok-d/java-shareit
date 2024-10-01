package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemWebResponceDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemWebResponceDto> json;

    @Test
    public void testItemRequestDtoSerialize() throws IOException {
        ItemWebResponceDto request = ItemWebResponceDto.builder()
                .id(1)
                .created(LocalDateTime.now())
                .description("description")
                .build();

        JsonContent<ItemWebResponceDto> content = json.write(request);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.created");
        assertThat(content).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("@.description").isEqualTo("description");
    }
}