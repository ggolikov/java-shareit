package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
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

    public UserDto updateUser(Integer id, UserDto userDto) {
        User updateUser = UserMapper.mapToUser(userDto);
        updateUser.setId(id);
        User user = null;

        if (updateUser.getEmail() != null) {
            user = userRepository.getUserByEmail(updateUser.getEmail());
        }

        if (user == null) {
            user = userRepository.getUser(id);
        }

        if (user == null) {
            return addUser(userDto);
        }

        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());

        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName());
        }

        return UserMapper.mapToUserDto(userRepository.updateUser(existingUser));
    }

    public void removeUser(Integer id) {
        userRepository.removeUser(id);
    }
}
