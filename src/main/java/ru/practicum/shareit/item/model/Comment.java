package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "text", nullable = false)
    String text;
    @Column(name = "item_id", nullable = false)
    Integer itemId;
    @Column(name = "author_id", nullable = false)
    Integer authorId;
}
