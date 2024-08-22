package ru.practicum.shareit.user;

public final class UserMapper {

    public static UserDto toUserDto (User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return dto;
    }
}
