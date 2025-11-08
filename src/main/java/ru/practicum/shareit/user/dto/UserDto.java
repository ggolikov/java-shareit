package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    public Integer id;
    public String name;
    @Email
    @NotNull
    public String email;
}
