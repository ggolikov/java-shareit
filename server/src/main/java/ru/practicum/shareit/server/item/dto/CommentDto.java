package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    Integer id;
    String text;
    LocalDateTime created;
    Item item;
    String authorName;
    User author;
}
