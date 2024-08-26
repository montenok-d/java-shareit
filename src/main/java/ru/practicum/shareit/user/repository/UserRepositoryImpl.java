package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long idCount = 0L;


    @Override
    public User create(User user) {
        user.setId(idCount);
        idCount += 1;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User update(User user, long id) {
        users.put(id, user);
        log.info("User id: {}", id);
        return users.get(id);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public void searchByEmail(User user) {
        for (User i : users.values()) {
            if (i.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Email must be unique");
            }
        }
    }
}
