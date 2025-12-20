package ru.practicum.shareit.server.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.server.item.model.Item;

import java.util.List;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;
    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    String description;
    @Column(name = "requestor_id", nullable = false)
    @Getter
    @Setter
    Integer requestorId;
    @Column(name = "created", nullable = false)
    @Getter
    @Setter
    LocalDateTime created;
    @OneToMany
    @JoinColumn(name = "request_id")
    @Getter
    @Setter
    List<Item> items;
}
