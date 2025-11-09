package ru.practicum.shareit.user.repository;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<Integer, User> users;

    public UserInMemoryRepository() {
        users = new HashMap<>();
    }

    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + "не найден");
        }
        return users.get(id);
    }

    public User getUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User addUser(User user) {
        validate(user);

        if (user.getId() == null) {
            user.setId(getNextId());
        }

        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        validate(user);

        users.put(user.getId(), user);
        return user;
    }

    public void removeUser(Integer id) {
        users.remove(id);
    }

    public void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Поле email не может быть пустым или null");
        }

        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()))) {
            throw new ValidationException("email уже занят");
        }
    }

    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
