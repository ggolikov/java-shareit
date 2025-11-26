package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;
    @Column(name = "text", nullable = false)
    @Getter
    @Setter
    String text;
    @Column(name = "created", nullable = false)
    @Getter
    @Setter
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @Getter
    @Setter
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Getter
    @Setter
    User author;

}
