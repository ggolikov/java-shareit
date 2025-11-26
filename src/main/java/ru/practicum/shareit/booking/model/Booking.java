package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;
    @Column(name = "start_date", nullable = false)
    @Getter
    @Setter
    LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    @Getter
    @Setter
    LocalDateTime end;
    @OneToOne
    @JoinColumn(name = "item_id")
    @Getter
    @Setter
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    @Getter
    @Setter
    User booker;
    @Column(name = "status")
    @Getter
    @Setter
    BookingStatus status;
}
