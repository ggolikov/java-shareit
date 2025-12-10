package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.user.model.User;

@Data
public class ItemDto {
    public Integer id;
    public String name;
    public String description;
    public Boolean available;
    public User owner;
    public Integer requestId;
}
