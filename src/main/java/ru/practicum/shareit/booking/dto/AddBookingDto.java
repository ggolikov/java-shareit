package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBookingDto {
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
}
