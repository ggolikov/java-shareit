package ru.practicum.shareit.server.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBookingDto {
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
}
