package ru.practicum.shareit.gateway.booking.dto;
import lombok.Data;
import ru.practicum.shareit.gateway.booking.model.BookingStatus;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    BookingStatus status;
}
