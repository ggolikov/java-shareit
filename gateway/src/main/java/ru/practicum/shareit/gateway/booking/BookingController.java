package ru.practicum.shareit.gateway.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.dto.AddBookingDto;
import ru.practicum.shareit.gateway.booking.model.BookingSearchStatus;
import ru.practicum.shareit.gateway.booking.service.BookingClient;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return client.getBooking(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid AddBookingDto addBookingDto) {
        return client.addBooking(userId, addBookingDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("approved") String approved) {
        return client.approveBooking(id, userId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getCurrentUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, value = "state", defaultValue = "ALL") BookingSearchStatus state) {
        return client.getCurrentUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, value = "state", defaultValue = "ALL") BookingSearchStatus state) {
        return client.getUserItemsBookings(userId, state);
    }
}
