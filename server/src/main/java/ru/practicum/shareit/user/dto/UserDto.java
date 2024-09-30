package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String email;
}
