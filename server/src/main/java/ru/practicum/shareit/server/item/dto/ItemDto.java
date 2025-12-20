package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.user.model.User;

@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Integer requestId;
}
