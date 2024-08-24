package ru.practicum.shareit.user;

public final class UserMapper {

    public static UserDto mapToUserDto (User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return dto;
    }

    public static User mapToUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        return user;
    }
}
