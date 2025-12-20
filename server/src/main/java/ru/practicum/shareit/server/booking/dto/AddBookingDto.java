package ru.practicum.shareit.server.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBookingDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer itemId;
}
