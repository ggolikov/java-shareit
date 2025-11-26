package ru.practicum.shareit.request.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "requestor_id", nullable = false)
    Integer requestorId;
}
