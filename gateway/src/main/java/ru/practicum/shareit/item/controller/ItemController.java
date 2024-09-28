package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        return itemClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.create(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                          @PathVariable("id") long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.delete(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.getAll(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search( @RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam("text") String text) {
        return itemClient.search(userId, text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable("id") long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
