package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> getById(long id);

    User update(User user, long id);

    void delete(long id);

    void searchByEmail(User user);
}
