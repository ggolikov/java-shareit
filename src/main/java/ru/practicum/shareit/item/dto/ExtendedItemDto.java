package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ExtendedItemDto extends ItemDto {
    LocalDateTime start;
    LocalDateTime end;
    Booking lastBooking;
    Booking nextBooking;
    Collection<CommentDto> comments;
}
