package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    String name;
    @Getter
    @Setter
    @Column(name = "description")
    String description;
    @Getter
    @Setter
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Getter
    @Setter
    User owner;
    @Column(name = "request_id")
    Integer requestId;
}
