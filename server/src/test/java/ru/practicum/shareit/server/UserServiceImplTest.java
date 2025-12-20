package ru.practicum.shareit.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;
import ru.practicum.shareit.server.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    // ----------------------------------------------------------------------
    // getUser()
    // ----------------------------------------------------------------------
    @Test
    void getUser_shouldReturnUserDto() {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setEmail("john@test.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto result = userService.getUser(1);

        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());
        verify(userRepository).findById(1);
    }

    @Test
    void getUser_shouldThrowNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUser(99));
    }

    // ----------------------------------------------------------------------
    // addUser()
    // ----------------------------------------------------------------------
    @Test
    void addUser_shouldSaveAndReturnUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setName("John");
        dto.setEmail("john@test.com");
        User user = UserMapper.mapToUser(dto);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.addUser(dto);

        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ----------------------------------------------------------------------
    // updateUser()
    // ----------------------------------------------------------------------
    @Test
    void updateUser_shouldUpdateNameAndEmail() {
        User existing = new User();
        existing.setId(1);
        existing.setName("New");
        existing.setEmail("old@test.com");
        User updated = new User();
        updated.setId(1);
        updated.setName("Updated");
        updated.setEmail("updated@test.com");

        UserDto updateDto = new UserDto();
        updateDto.setName("New");
        updateDto.setEmail("new@test.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        UserDto result = userService.updateUser(1, updateDto);

        assertEquals("Updated", result.getName());
        assertEquals("updated@test.com", result.getEmail());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("New", saved.getName());
        assertEquals("new@test.com", saved.getEmail());
    }

    @Test
    void updateUser_shouldThrowNotFoundIfUserMissing() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(null, new UserDto()));
    }

    // ----------------------------------------------------------------------
    // removeUser()
    // ----------------------------------------------------------------------
    @Test
    void removeUser_shouldCallDelete() {
        userService.removeUser(5);
        verify(userRepository).deleteById(5);
    }
}
