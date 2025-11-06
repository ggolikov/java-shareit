package ru.practicum.shareit.user.repository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    public User getUser(Integer id);

    public User getUserByEmail(String email);

    public User addUser(@Valid User user);

    public User updateUser(@Valid User user);

    public void removeUser(Integer id);

    public  void validate(User user) throws ValidationException;
}
