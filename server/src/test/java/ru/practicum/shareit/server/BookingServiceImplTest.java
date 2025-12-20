package ru.practicum.shareit.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.booking.dto.AddBookingDto;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingSearchStatus;
import ru.practicum.shareit.server.booking.model.BookingStatus;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.booking.service.BookingServiceImpl;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.exception.ValidationException;
import ru.practicum.shareit.server.item.dto.ExtendedItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingServiceImpl bookingService;

    @BeforeEach
    void init() {
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);

        bookingService = new BookingServiceImpl(
                bookingRepository,
                userRepository,
                itemRepository
        );
    }

    // ---------------------------------------------------------------------
    // getBooking()
    // ---------------------------------------------------------------------

    @Test
    void getBooking_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("booker@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(1, 100);

        assertEquals(1, result.getId());
        verify(bookingRepository).findById(100);
    }

    @Test
    void getBooking_itemUnavailable() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("booker@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(false);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.getBooking(1, 100));
    }

    @Test
    void getBooking_notOwnerOrBooker() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("owner@test.com");
        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("booker@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.getBooking(33, 100));
    }

    // ---------------------------------------------------------------------
    // addBooking()
    // ---------------------------------------------------------------------

    @Test
    void addBooking_success() {
        AddBookingDto dto = new AddBookingDto();
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));
        dto.setItemId(10);

        User booker = new User();
        booker.setId(5);
        booker.setName("user");
        booker.setEmail("u@test.com");

        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        when(itemRepository.findById(10)).thenReturn(Optional.of(item));
        when(userRepository.findById(5)).thenReturn(Optional.of(booker));

        Booking savedBooking = new Booking();
        savedBooking.setId(100);
        savedBooking.setStart(LocalDateTime.now());
        savedBooking.setEnd(LocalDateTime.now().plusDays(1));
        savedBooking.setStatus(BookingStatus.WAITING);
        savedBooking.setItem(item);
        savedBooking.setBooker(booker);

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingDto result = bookingService.addBooking(5, dto);

        assertEquals(100, result.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void addBooking_itemNotFound() {
        AddBookingDto dto = new AddBookingDto();
        when(itemRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(5, dto));
    }

    @Test
    void addBooking_userNotFound() {
        AddBookingDto dto = new AddBookingDto();
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        User owner = new User();
        owner.setId(1);
        owner.setName("o");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        when(itemRepository.findById(10)).thenReturn(Optional.of(item));
        when(userRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(5, dto));
    }

    @Test
    void addBooking_itemUnavailable() {
        User owner = new User();
        owner.setId(5);
        owner.setName("o");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(false);
        item.setOwner(owner);

        AddBookingDto dto = new AddBookingDto();
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));
        dto.setItemId(item.getId());

        when(itemRepository.findById(10)).thenReturn(Optional.of(item));
        when(userRepository.findById(5)).thenReturn(Optional.of(owner));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(5, dto));
    }

    // ---------------------------------------------------------------------
    // approveBooking()
    // ---------------------------------------------------------------------

    @Test
    void approveBooking_approved() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("owner@test.com");

        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("b@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(owner);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingDto result = bookingService.approveBooking(100, 1, "true");

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void approveBooking_rejected() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("owner@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("b@test.com");

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingDto result = bookingService.approveBooking(100, 1, "false");

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void approveBooking_notOwner() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("b@test.com");

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(100, 999, "true"));
    }

    @Test
    void approveBooking_itemUnavailable() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(false);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(100);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(100, 1, "true"));
    }

    // ---------------------------------------------------------------------
    // getUserBookings()
    // ---------------------------------------------------------------------

    @Test
    void getUserBookings_waiting() {
        Booking b1 = new Booking();
        b1.setStatus(BookingStatus.WAITING);

        Booking b2 = new Booking();
        b2.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.getUserBookings(5)).thenReturn(List.of(b1, b2));

        List<BookingDto> result = (List<BookingDto>) bookingService.getUserBookings(5, BookingSearchStatus.WAITING);

        assertEquals(1, result.size());
    }

    @Test
    void getUserBookings_rejected() {
        Booking b1 = new Booking();
        b1.setStatus(BookingStatus.REJECTED);

        Booking b2 = new Booking();
        b2.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.getUserBookings(5)).thenReturn(List.of(b1, b2));

        List<BookingDto> result = (List<BookingDto>) bookingService.getUserBookings(5, BookingSearchStatus.REJECTED);

        assertEquals(1, result.size());
    }

    @Test
    void getUserBookings_current() {
        Booking b1 = new Booking();
        b1.setStart(LocalDateTime.now().minusDays(1));
        b1.setEnd(LocalDateTime.now().plusDays(1));

        Booking b2 = new Booking();
        b2.setStart(LocalDateTime.now().minusDays(2));
        b2.setEnd(LocalDateTime.now().minusDays(1));

        when(bookingRepository.getUserBookings(5)).thenReturn(List.of(b1, b2));

        List<BookingDto> result = (List<BookingDto>) bookingService.getUserBookings(5, BookingSearchStatus.CURRENT);

        assertEquals(1, result.size());
    }

    @Test
    void getUserBookings_past() {
        Booking b1 = new Booking();
        b1.setStart(LocalDateTime.now().minusDays(1));
        b1.setEnd(LocalDateTime.now().plusDays(1));

        Booking b2 = new Booking();
        b2.setStart(LocalDateTime.now().minusDays(2));
        b2.setEnd(LocalDateTime.now().minusDays(1));

        when(bookingRepository.getUserBookings(5)).thenReturn(List.of(b1, b2));

        List<BookingDto> result = (List<BookingDto>) bookingService.getUserBookings(5, BookingSearchStatus.PAST);

        assertEquals(1, result.size());
    }


    @Test
    void getUserBookings_future() {
        Booking b1 = new Booking();
        b1.setStart(LocalDateTime.now().plusDays(1));
        b1.setEnd(LocalDateTime.now().plusDays(2));

        Booking b2 = new Booking();
        b2.setStart(LocalDateTime.now().minusDays(2));
        b2.setEnd(LocalDateTime.now().minusDays(1));

        when(bookingRepository.getUserBookings(5)).thenReturn(List.of(b1, b2));

        List<BookingDto> result = (List<BookingDto>) bookingService.getUserBookings(5, BookingSearchStatus.FUTURE);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------
    // getUserItemsBookings()
    // ---------------------------------------------------------------------

    @Test
    void getUserItemsBookings_userHasNoItems() {
        when(itemRepository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundException.class,
                () -> bookingService.getUserItemsBookings(5, BookingSearchStatus.ALL));
    }

    @Test
    void getUserItemsBookings_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extItem = new ExtendedItemDto();
        extItem.setId(10);
        extItem.setName("item");
        extItem.setDescription("desc");
        extItem.setAvailable(true);
        extItem.setOwner(owner);

        Booking b = new Booking();
        b.setStatus(BookingStatus.WAITING);
        b.setStart(LocalDateTime.now().plusHours(1));
        b.setEnd(LocalDateTime.now().plusDays(1));

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.getUserItemsBookings(1)).thenReturn(List.of(b));

        List<BookingDto> result =
                (List<BookingDto>) bookingService.getUserItemsBookings(1, BookingSearchStatus.WAITING);

        assertEquals(1, result.size());
    }

    @Test
    void getUserItemsBookings_rejected() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extItem = new ExtendedItemDto();
        extItem.setId(10);
        extItem.setName("item");
        extItem.setDescription("desc");
        extItem.setAvailable(true);
        extItem.setOwner(owner);

        Booking b = new Booking();
        b.setStatus(BookingStatus.REJECTED);
        b.setStart(LocalDateTime.now().plusHours(1));
        b.setEnd(LocalDateTime.now().plusDays(1));

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.getUserItemsBookings(1)).thenReturn(List.of(b));

        List<BookingDto> result =
                (List<BookingDto>) bookingService.getUserItemsBookings(1, BookingSearchStatus.REJECTED);

        assertEquals(1, result.size());
    }

    @Test
    void getUserItemsBookings_current() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extItem = new ExtendedItemDto();
        extItem.setId(10);
        extItem.setName("item");
        extItem.setDescription("desc");
        extItem.setAvailable(true);
        extItem.setOwner(owner);

        Booking b = new Booking();
        b.setStatus(BookingStatus.REJECTED);
        b.setStart(LocalDateTime.now().minusDays(1));
        b.setEnd(LocalDateTime.now().plusDays(1));

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.getUserItemsBookings(1)).thenReturn(List.of(b));

        List<BookingDto> result =
                (List<BookingDto>) bookingService.getUserItemsBookings(1, BookingSearchStatus.CURRENT);

        assertEquals(1, result.size());
    }

    @Test
    void getUserItemsBookings_past() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extItem = new ExtendedItemDto();
        extItem.setId(10);
        extItem.setName("item");
        extItem.setDescription("desc");
        extItem.setAvailable(true);
        extItem.setOwner(owner);

        Booking b = new Booking();
        b.setStatus(BookingStatus.REJECTED);
        b.setStart(LocalDateTime.now().minusDays(2));
        b.setEnd(LocalDateTime.now().minusDays(1));

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.getUserItemsBookings(1)).thenReturn(List.of(b));

        List<BookingDto> result =
                (List<BookingDto>) bookingService.getUserItemsBookings(1, BookingSearchStatus.PAST);

        assertEquals(1, result.size());
    }

    @Test
    void getUserItemsBookings_future() {
        User owner = new User();
        owner.setId(1);
        owner.setName("owner");
        owner.setEmail("o@test.com");

        Item item = new Item();
        item.setId(10);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extItem = new ExtendedItemDto();
        extItem.setId(10);
        extItem.setName("item");
        extItem.setDescription("desc");
        extItem.setAvailable(true);
        extItem.setOwner(owner);

        Booking b = new Booking();
        b.setStatus(BookingStatus.REJECTED);
        b.setStart(LocalDateTime.now().plusDays(1));
        b.setEnd(LocalDateTime.now().plusDays(2));

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.getUserItemsBookings(1)).thenReturn(List.of(b));

        List<BookingDto> result =
                (List<BookingDto>) bookingService.getUserItemsBookings(1, BookingSearchStatus.FUTURE);

        assertEquals(1, result.size());
    }
}
