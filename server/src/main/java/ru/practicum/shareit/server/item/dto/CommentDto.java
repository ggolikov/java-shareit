package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Integer id;
    private String text;
    private LocalDateTime created;
    private Item item;
    private String authorName;
    private User author;
}
