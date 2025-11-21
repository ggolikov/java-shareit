package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "is_available")
    Boolean available;
    @Column(name = "owner_id")
    Integer ownerId;
    @Column(name = "request_id")
    Integer requestId;
}
