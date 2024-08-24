package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> getById(long id);

    User update(User user);

    void delete(long id);
}
