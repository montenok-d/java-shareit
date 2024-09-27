package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final private ItemRequestService requestService;

    @GetMapping("/{id}")
    public ItemRequestDto findById(@PathVariable("id") long id) {
        return requestService.findById(id);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return requestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        return requestService.findAll();
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                 @RequestBody ItemRequestDto request) {
        return requestService.create(ownerId, request);
    }
}
