package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
