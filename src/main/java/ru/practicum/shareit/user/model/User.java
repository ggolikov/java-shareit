package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;
    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    String name;
    @Column(name = "email")
    @Getter
    @Setter
    String email;
}
