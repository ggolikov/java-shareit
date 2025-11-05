package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto getUser(Integer id);
    UserDto addUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void removeUser(Integer id);
}
