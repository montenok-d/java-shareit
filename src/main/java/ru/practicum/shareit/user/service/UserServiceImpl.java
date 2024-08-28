package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getById(long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", id)));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        userRepository.searchByEmail(UserMapper.mapToUser(userDto));
        User user = userRepository.create(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = checkUserExists(id);
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            userRepository.searchByEmail(UserMapper.mapToUser(userDto));
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        User newUser = userRepository.update(user, id);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    public User checkUserExists(long id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", id)));
    }
}
