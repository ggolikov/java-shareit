package ru.practicum.shareit.server.item.dto;

import lombok.Data;

@Data
public class AddItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
