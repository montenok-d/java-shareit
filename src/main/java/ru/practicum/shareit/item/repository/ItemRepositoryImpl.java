package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long idCount = 0L;

    public Optional<Item> getById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Item create(Item item) {
        item.setId(idCount);
        idCount += 1;
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item, long itemId) {
        items.put(itemId, item);
        return item;
    }

    public void delete(long itemId) {
        items.remove(itemId);
    }

    @Override
    public List<Item> getAll(long ownerId) {
        List<Item> ownersItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == ownerId)
                ownersItems.add(item);
        }
        return ownersItems;
    }

    public List<Item> search(String text) {
        log.info("All items: {}", items);
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable().equals(true)
                )
                .collect(Collectors.toList());
    }
}
