package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;
    private String text;
    private String authorName;
    private long item;
    private LocalDateTime created;
}
