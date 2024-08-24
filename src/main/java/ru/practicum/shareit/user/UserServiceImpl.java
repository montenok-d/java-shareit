package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getById(long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User № %d not found", id)));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.create(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        checkUserExists(id);
        User user = userRepository.update(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void delete(long id) {
        checkUserExists(id);
        userRepository.delete(id);
    }

    public User checkUserExists(long id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User № %d not found", id)));
    }
}
