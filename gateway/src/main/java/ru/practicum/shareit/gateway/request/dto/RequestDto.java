package ru.practicum.shareit.gateway.request.dto;

import lombok.Data;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDto {
    Integer id;
    String description;
    LocalDateTime created;
    List<ItemDto> items;
}
