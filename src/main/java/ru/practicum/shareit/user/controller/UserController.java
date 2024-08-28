package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("id") long id) {
        return userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
