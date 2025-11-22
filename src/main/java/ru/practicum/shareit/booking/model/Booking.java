package ru.practicum.shareit.booking.model;

import lombok.Data;
import jakarta.persistence.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "start_date", nullable = false)
    LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    LocalDateTime end;
    @OneToOne
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;
    @Column(name = "status")
    BookingStatus status;
}
