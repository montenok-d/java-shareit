package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {

    ItemDto getById(long id);

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long itemId, long userId);

    void delete(long id, long userId);

    List<ItemWithBookingDto> getAll(long userId);

    List<ItemDto> search(String text);

    CommentDto addComment(long itemId, long userId, CommentDto commentdto);
}
