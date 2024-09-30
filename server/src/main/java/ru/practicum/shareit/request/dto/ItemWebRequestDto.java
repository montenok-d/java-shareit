package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class ItemWebRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
}