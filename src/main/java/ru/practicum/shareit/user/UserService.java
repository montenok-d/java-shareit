package ru.practicum.shareit.user;

public interface UserService {

    UserDto getById(long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long id);

    void delete(long id);
}
