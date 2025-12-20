package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.user.dto.UserDto;

public interface UserService {
    UserDto getUser(Integer id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Integer id, UserDto userDto);

    void removeUser(Integer id);
}
