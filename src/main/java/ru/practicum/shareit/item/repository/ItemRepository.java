package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {


    Optional<Item> getById(long id);

    Item create(Item item);

    Item update(Item item, long itemId);

    void delete(long itemId);

    List<Item> getAll(long ownerId);

    List<Item> search(String text);
}
