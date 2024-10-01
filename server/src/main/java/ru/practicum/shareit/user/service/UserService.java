package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto findById(long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long id);

    void delete(long id);
}
