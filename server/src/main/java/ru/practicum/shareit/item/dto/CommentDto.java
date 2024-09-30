package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class CommentDto {
    private long id;
    private String text;
    private String authorName;
    private long item;
    private LocalDateTime created;
}
