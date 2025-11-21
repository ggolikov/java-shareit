package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        UserDto existingUserDto = getUser(id);

        if (existingUserDto != null) {
            if (userDto.getName() != null) {
                existingUserDto.setName(userDto.getName());
            }

            if (userDto.getEmail() != null) {
                existingUserDto.setEmail(userDto.getEmail());
            }

            User updatingUser = userRepository.save(UserMapper.mapToUser(existingUserDto));

            return UserMapper.mapToUserDto(updatingUser);
        }

        throw new NotFoundException("User not found");
    }

    public void removeUser(Integer id) {
        userRepository.deleteById(id);
    }
}
