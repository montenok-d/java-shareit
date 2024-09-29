package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService requestService;

    @GetMapping("/{id}")
    public ItemRequestDto findById(@PathVariable("id") long id) {
        log.info("Get itemRequest, id: {}", id);
        return requestService.findById(id);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Get itemRequests by owner, ownerId: {}", ownerId);
        return requestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        log.info("Get all itemRequests");
        return requestService.findAll();
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody ItemRequestDto request) {
        log.info("Create itemRequest: {}, userId: {}", request, ownerId);
        return requestService.create(ownerId, request);
    }
}
