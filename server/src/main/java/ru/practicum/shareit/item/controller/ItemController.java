package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") long id) {
        log.info("Get item, userId: {}", id);
        return itemService.getById(id);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Create item: {}, userId: {}", itemDto, ownerId);
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable("id") long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Update item. ItemDto {}, itemId: {}, userId: {}", itemDto, itemId, userId);
        return itemService.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Delete item, itemId {}, userId: {}", itemId, userId);
        itemService.delete(itemId, userId);
    }

    @GetMapping
    public List<ItemWithBookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Get items by owner, ownerId: {}", ownerId);
        return itemService.getAll(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Search item by text, text: {}", text);
        return itemService.search(text);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable("id") long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Add comment, userId: {}, itemId {}, comment: {}", userId, itemId, commentDto);
        return itemService.addComment(itemId, userId, commentDto);
    }
}
