package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ExtendedItemDto extends ItemDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Booking lastBooking;
    private Booking nextBooking;
    private Collection<CommentDto> comments;
}
