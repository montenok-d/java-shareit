package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class UserServiceTest {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = UserDto.builder()
                .name("Name")
                .email("mail@mail.ru")
                .build();
    }

    @Test
    void createTest() {
        UserDto userDto = UserDto.builder()
                .name("Name")
                .email("mail@mail.ru")
                .build();

        UserDto createdUser = userService.create(userDto);

        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(userDto.getName(), userDto.getName());
    }

    @Test
    void updateTest() {
        UserDto userDto = userService.create(user);
        user.setEmail("greg@ya.ru");

        userDto = userService.update(user, userDto.getId());
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userService.create(user);

        userService.delete(userDto.getId());

        assertThat(userRepository.findById(user.getId()).isEmpty());
    }

    @Test
    void findByIdTest() {
        UserDto userDto = userService.create(user);

        UserDto returnedUser = userService.findById(userDto.getId());

        assertEquals(userDto.getId(), returnedUser.getId());
        assertEquals(userDto.getName(), returnedUser.getName());
        assertEquals(userDto.getEmail(), returnedUser.getEmail());
    }
}