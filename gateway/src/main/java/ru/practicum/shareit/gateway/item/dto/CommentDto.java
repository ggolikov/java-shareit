package ru.practicum.shareit.gateway.item.dto;

import lombok.Data;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    Integer id;
    String text;
    LocalDateTime created;
    ItemDto item;
    String authorName;
    UserDto author;
}
