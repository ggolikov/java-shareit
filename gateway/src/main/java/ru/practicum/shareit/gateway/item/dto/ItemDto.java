package ru.practicum.shareit.gateway.item.dto;

import lombok.Data;
import ru.practicum.shareit.gateway.user.dto.UserDto;

@Data
public class ItemDto {
    public Integer id;
    public String name;
    public String description;
    public Boolean available;
    public UserDto owner;
    public Integer requestId;
}
