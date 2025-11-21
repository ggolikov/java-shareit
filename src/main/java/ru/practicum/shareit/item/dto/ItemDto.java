package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
public class ItemDto {
    public Integer id;
    public String name;
    public String description;
    public Boolean available;
    public Integer ownerId;
    public ItemRequest request;
}
