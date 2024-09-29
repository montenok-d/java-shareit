package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemDto item = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .owner(1)
                .requestId(1)
                .build();

        JsonContent<ItemDto> result = json.write(item);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.requestId")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.name");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(item.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(item.getName()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(item.getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(item_available -> assertThat(item_available).isEqualTo(item.getAvailable()));
    }
}