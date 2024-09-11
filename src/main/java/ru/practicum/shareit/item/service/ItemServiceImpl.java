package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getById(long id) {
        Item item = itemRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", id)));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto create(ItemDto itemDto, long ownerId) {
        User user = userRepository.getById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", ownerId)));
        itemDto.setOwner(ownerId);
        Item item = itemRepository.create(ItemMapper.mapToItem(itemDto));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long itemId, long userId) {
        Item item = checkItemExists(itemId);
        if (item.getOwner() != userId) {
            throw new EntityNotFoundException("Access denied");
        }
        itemDto.setId(itemId);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.update(item,itemId);
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public void delete(long itemId, long userId) {
        Item item = checkItemExists(itemId);
        if (item.getOwner() != userId) {
            throw new EntityNotFoundException("Access denied");
        }
        itemRepository.delete(itemId);
    }

    @Override
    public List<ItemDto> getAll(long ownerId) {
        List<Item> allItems = itemRepository.getAll(ownerId);
        return allItems.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> searchResults = itemRepository.search(text);
        return searchResults.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    public Item checkItemExists(long id) {
        return itemRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", id)));
    }
}
