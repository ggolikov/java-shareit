package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Data
public class ItemDto {
    public Integer id;
    public String name;
    public String description;
    public Boolean available;
    public User owner;
    public Request request;
}
