package ru.practicum.shareit.server.request.dto;

import lombok.Data;
import ru.practicum.shareit.server.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDto {
    Integer id;
    String description;
    LocalDateTime created;
    List<ItemDto> items;
}
