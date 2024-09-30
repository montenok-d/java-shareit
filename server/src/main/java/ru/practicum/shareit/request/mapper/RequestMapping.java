package ru.practicum.shareit.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemWebResponceDto;
import ru.practicum.shareit.request.dto.ItemWebRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@NoArgsConstructor
public final class RequestMapping {

    public static ItemWebResponceDto mapToRequestDto(ItemRequest request) {
        return ItemWebResponceDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static ItemRequest mapToRequest(ItemWebRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .created(dto.getCreated())
                .build();
    }
}
