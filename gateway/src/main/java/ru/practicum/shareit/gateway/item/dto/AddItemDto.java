package ru.practicum.shareit.gateway.item.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class AddItemDto {
    public String name;
    public String description;
    public Boolean available;
    public Long ownerId;
    @Nullable
    public Integer requestId;
}
