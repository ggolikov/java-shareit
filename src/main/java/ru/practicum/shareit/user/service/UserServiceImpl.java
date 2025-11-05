package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Qualifier("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto getUser(Integer id) {
        return UserMapper.mapToUserDto(userRepository.getUser(id));
    }

    public UserDto addUser(UserDto userDto) {
        return UserMapper.mapToUserDto(userRepository.addUser(UserMapper.mapToUser(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        return UserMapper.mapToUserDto(userRepository.updateUser(UserMapper.mapToUser(userDto)));
    }

    public void removeUser(Integer id) {
        userRepository.removeUser(id);
    }
}
