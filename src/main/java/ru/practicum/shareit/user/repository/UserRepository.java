package ru.practicum.shareit.user.repository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User getUser(Integer id);

    User getUserByEmail(String email);

    User addUser(@Valid User user);

    User updateUser(@Valid User user);

    void removeUser(Integer id);

    void validate(User user) throws ValidationException;
}
