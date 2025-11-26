package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;

import java.util.Collection;

public interface BookingService {
    BookingDto getBooking(Integer userId, Integer bookingId);

    BookingDto addBooking(Integer userId, AddBookingDto addBookingDto);

    BookingDto approveBooking(Integer bookingId, Integer userId, String approved);

    Collection<BookingDto> getUserBookings(Integer userId, BookingSearchStatus state);

    Collection<BookingDto> getUserItemsBookings(Integer userId, BookingSearchStatus state);
}
