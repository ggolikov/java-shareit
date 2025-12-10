package ru.practicum.shareit.gateway.item.dto;

import lombok.Data;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ExtendedItemDto extends ItemDto {
    LocalDateTime start;
    LocalDateTime end;
    BookingDto lastBooking;
    BookingDto nextBooking;
    Collection<CommentDto> comments;
}
