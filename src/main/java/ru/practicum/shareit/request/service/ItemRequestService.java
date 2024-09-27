package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto findById(long id);

    List<ItemRequestDto> findAllByOwner(long ownerId);

    List<ItemRequestDto> findAll();

    ItemRequestDto create(long ownerId, ItemRequestDto request);
}
