package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
        return bookingService.getBooking(userId, id);
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody @Valid AddBookingDto addBookingDto) {
        return bookingService.addBooking(userId, addBookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDto approveBooking(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam("approved") String approved) {
        return bookingService.approveBooking(id, userId, approved);
    }

    @GetMapping
    public Collection<BookingDto> getCurrentUserBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,@RequestParam(required = false, value = "state", defaultValue = "ALL") BookingSearchStatus state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getUserItemsBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,@RequestParam(required = false, value = "state", defaultValue = "ALL") BookingSearchStatus state) {
        return bookingService.getUserItemsBookings(userId, state);
    }
}
