package ru.practicum.shareit.gateway.item.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class AddItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    @Nullable
    private Integer requestId;
}
