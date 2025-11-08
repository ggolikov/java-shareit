package ru.practicum.shareit.user.repository;

import jakarta.validation.ValidationException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User getUser(Integer id);

    User getUserByEmail(String email);

    User addUser(User user);

    User updateUser(User user);

    void removeUser(Integer id);

    void validate(User user) throws ValidationException;
}
