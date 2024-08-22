package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getById(long id);

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemDto itemDto, long itemId, long ownerId);

    void delete(long id, long ownerId);

    List<ItemDto> getAll(long ownerId);

    List<ItemDto> search(String text);
}
