package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemWebRequestDto;
import ru.practicum.shareit.request.dto.ItemWebResponceDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService requestService;

    @GetMapping("/{id}")
    public ItemWebResponceDto findById(@PathVariable("id") long id) {
        log.info("Get itemRequest, id: {}", id);
        return requestService.findById(id);
    }

    @GetMapping
    public List<ItemWebResponceDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Get itemRequests by owner, ownerId: {}", ownerId);
        return requestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemWebResponceDto> findAll() {
        log.info("Get all itemRequests");
        return requestService.findAll();
    }

    @PostMapping
    public ItemWebResponceDto create(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody ItemWebRequestDto request) {
        log.info("Create itemRequest: {}, userId: {}", request, ownerId);
        return requestService.create(ownerId, request);
    }
}
