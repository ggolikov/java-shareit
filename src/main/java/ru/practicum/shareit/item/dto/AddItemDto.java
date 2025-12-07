package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class AddItemDto {
    public String name;
    public String description;
    public Boolean available;
    public Integer requestId;
}
