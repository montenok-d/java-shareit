package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemWebRequestDto;
import ru.practicum.shareit.request.dto.ItemWebResponceDto;

import java.util.List;

public interface ItemRequestService {
    ItemWebResponceDto findById(long id);

    List<ItemWebResponceDto> findAllByOwner(long ownerId);

    List<ItemWebResponceDto> findAll();

    ItemWebResponceDto create(long ownerId, ItemWebRequestDto request);
}
