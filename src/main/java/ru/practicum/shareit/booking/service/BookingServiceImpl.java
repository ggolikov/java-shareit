package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("bookingService")
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto getBooking(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        Item item = booking.getItem();
        User owner = item.getOwner();
        User booker = booking.getBooker();

        if (item.getAvailable() == false) {
            throw new ValidationException("Not available");
        }

        if (owner.getId() != userId && booker.getId() != userId) {
            throw new ValidationException("Not owner of booking");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    public BookingDto addBooking(Integer userId, AddBookingDto addBookingDto) {
        Integer itemId = addBookingDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (item.getAvailable() == false) {
            throw new ValidationException("Not available");
        }

        Booking addedBooking = new Booking();
        addedBooking.setBooker(booker);
        addedBooking.setItem(item);
        addedBooking.setStart(addBookingDto.getStart());
        addedBooking.setEnd(addBookingDto.getEnd());
        addedBooking.setStatus(BookingStatus.WAITING);

        Booking booking = bookingRepository.save(addedBooking);

        return BookingMapper.mapToBookingDto(booking);
    }

    public BookingDto approveBooking(Integer bookingId, Integer userId, String approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        Item item = booking.getItem();

        if (item.getAvailable() == false) {
            throw new ValidationException("Not available");
        }

        if (item.getOwner().getId() != userId) {
            throw new ValidationException("Not owner of booking");
        }

        if (approved.equals("true")) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (approved.equals("false")) {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.mapToBookingDto(updatedBooking);
    }

    public Collection<BookingDto> getUserBookings(Integer userId, BookingSearchStatus state) {
        Collection<Booking> bookings = bookingRepository.getUserBookings(userId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case WAITING:
                bookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.WAITING).toList();
                break;
            case REJECTED:
                bookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.REJECTED).toList();
                break;
            case CURRENT:
                bookings = bookings.stream().filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now)).toList();
                break;
            case PAST:
                bookings = bookings.stream().filter(b -> b.getStart().isBefore(now) && b.getEnd().isBefore(now)).toList();
                break;
            case FUTURE:
                bookings = bookings.stream().filter(b -> b.getStart().isAfter(now) && b.getEnd().isAfter(now)).toList();
                break;
            default:
                break;
        }

        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    public Collection<BookingDto> getUserItemsBookings(Integer userId, BookingSearchStatus state) {
        Collection<Booking> bookings = bookingRepository.getUserItemsBookings(userId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case WAITING:
                bookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.WAITING).toList();
                break;
            case REJECTED:
                bookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.REJECTED).toList();
                break;
            case CURRENT:
                bookings = bookings.stream().filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now)).toList();
                break;
            case PAST:
                bookings = bookings.stream().filter(b -> b.getStart().isBefore(now) && b.getEnd().isBefore(now)).toList();
                break;
            case FUTURE:
                bookings = bookings.stream().filter(b -> b.getStart().isAfter(now) && b.getEnd().isAfter(now)).toList();
                break;
            default:
                break;
        }

        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }
}
