package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@NoArgsConstructor
public final class RequestMapping {

    public static ItemRequestDto toItemRequestDto (ItemRequest itemRequest) {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
        return dto;
    }
}
