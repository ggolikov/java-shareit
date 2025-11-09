package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.removeUser(id);
    }
}
