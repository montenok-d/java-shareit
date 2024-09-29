package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("дрель")
                .description("description")
                .available(true)
                .owner(1L)
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("qwer")
                .item(1L)
                .build();
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        when(itemService.getById(anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class));
        verify(itemService, times(1)).getById(anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void createItemTest() {
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class));
        verify(itemService, times(1)).create(any(), anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void updateItemTest() {
        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class));
        verify(itemService, times(1)).update(any(), anyLong(), anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void deleteItemTest() {
        mvc.perform(MockMvcRequestBuilders.delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
        verify(itemService, times(1)).delete(anyLong(), anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest() {
        List<ItemWithBookingDto> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemWithBookingDto item = ItemWithBookingDto.builder()
                    .id(i)
                    .name("name " + i)
                    .description("description")
                    .available(true)
                    .owner(1L)
                    .build();
            items.add(item);
        }

        when(itemService.getAll(anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)));
        verify(itemService, times(1)).getAll(anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void searchTest() {
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemDto item = ItemDto.builder()
                    .id(i)
                    .name("name " + i)
                    .description("description")
                    .available(true)
                    .owner(1L)
                    .build();
            items.add(item);
        }

        when(itemService.search(any()))
                .thenReturn(items);

        mvc.perform(get("/items/search?text=gre3")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)));
        verify(itemService, times(1)).search(any());
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void createCommentTest() {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item", is(commentDto.getItem()), Long.class));
        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any());
        verifyNoMoreInteractions(itemService);
    }
}