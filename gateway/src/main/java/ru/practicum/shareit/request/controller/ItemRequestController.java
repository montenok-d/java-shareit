package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long id) {
        return requestClient.findById(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return requestClient.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.findAll(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                         @RequestBody ItemRequestDto request) {
        return requestClient.create(ownerId, request);
    }
}
