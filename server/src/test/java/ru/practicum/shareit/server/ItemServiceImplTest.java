package ru.practicum.shareit.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.exception.ValidationException;
import ru.practicum.shareit.server.item.dto.AddItemDto;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ExtendedItemDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.CommentRepository;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.item.service.ItemServiceImpl;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        commentRepository = mock(CommentRepository.class);
        bookingRepository = mock(BookingRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository);
    }

    // -------------------------------------------------------------------
    // getItem()
    // -------------------------------------------------------------------
    @Test
    void getItem_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

//        ExtendedItemDto extendedItemDto = new ExtendedItemDto(1, "Item1", "Description", true, new User(1, "Owner", "owner@test.com"));
        Booking lastBooking = new Booking();
        lastBooking.setId(1);
        lastBooking.setStart(LocalDateTime.now());
        lastBooking.setEnd(LocalDateTime.now().plusDays(1));
        Booking nextBooking = new Booking();
        nextBooking.setId(2);
        nextBooking.setStart(LocalDateTime.now());
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.getItemBookings(1)).thenReturn(Arrays.asList(lastBooking, nextBooking));
        when(commentRepository.getItemComments(1)).thenReturn(Collections.emptyList());

        ExtendedItemDto result = itemService.getItem(1);

        assertNotNull(result);
        assertEquals("Item1", result.getName());
        assertEquals("Description", result.getDescription());
        verify(itemRepository).findById(1);
    }

    @Test
    void getItem_itemNotFound() {
        when(itemRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItem(1));
    }

    // -------------------------------------------------------------------
    // addItem()
    // -------------------------------------------------------------------
    @Test
    void addItem_success() {
        AddItemDto addItemDto = new AddItemDto();
        addItemDto.setName("Item1");
        addItemDto.setDescription("Description");
        addItemDto.setAvailable(true);
        addItemDto.setRequestId(1);

        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(1, addItemDto);

        assertNotNull(result);
        assertEquals("Item1", result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addItem_nameEmpty() {
        AddItemDto addItemDto = new AddItemDto();
        addItemDto.setName("");
        addItemDto.setDescription("Description");
        addItemDto.setAvailable(true);
        addItemDto.setRequestId(1);

        assertThrows(ValidationException.class, () -> itemService.addItem(1, addItemDto));
    }

    @Test
    void addItem_userNotFound() {
        AddItemDto addItemDto = new AddItemDto();
        addItemDto.setName("Item1");
        addItemDto.setDescription("Description");
        addItemDto.setAvailable(true);
        addItemDto.setRequestId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(null, addItemDto));
    }

    // -------------------------------------------------------------------
    // updateItem()
    // -------------------------------------------------------------------
    @Test
    void updateItem_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item existingItem = new Item();
        existingItem.setId(1);
        existingItem.setName("Old Item");
        existingItem.setDescription("Old Description");
        existingItem.setAvailable(true);
        existingItem.setOwner(owner);

        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setId(1);
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setDescription("Updated Description");
        updatedItemDto.setAvailable(true);

        Item updatedItem = new Item();
        updatedItem.setId(existingItem.getId());
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");
        updatedItem.setAvailable(true);
        updatedItem.setOwner(owner);

        when(itemRepository.findById(1)).thenReturn(Optional.of(existingItem));
        when(userRepository.findById(1)).thenReturn(Optional.of(existingItem.getOwner()));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        ItemDto result = itemService.updateItem(1, 1, updatedItemDto);

        assertEquals("Updated Item", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_itemNotFound() {
        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setId(1);
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setDescription("Updated Description");
        updatedItemDto.setAvailable(true);

        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1, 1, updatedItemDto));
    }

    @Test
    void updateItem_userNotFound() {
        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setId(1);
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setDescription("Updated Description");
        updatedItemDto.setAvailable(true);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1, null, updatedItemDto));
    }

    // -------------------------------------------------------------------
    // getItems()
    // -------------------------------------------------------------------
    @Test
    void getItems_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        ExtendedItemDto extendedItemDto = new ExtendedItemDto();
        extendedItemDto.setId(1);
        extendedItemDto.setName("Item1");
        extendedItemDto.setDescription("Description");
        extendedItemDto.setAvailable(true);
        extendedItemDto.setOwner(owner);

        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
        when(bookingRepository.getItemBookings(1)).thenReturn(Collections.emptyList());
        when(commentRepository.getItemComments(1)).thenReturn(Collections.emptyList());

        Collection<ExtendedItemDto> result = itemService.getItems(1);

        assertEquals(1, result.size());
        assertEquals("Item1", result.iterator().next().getName());
    }

    @Test
    void getItems_userNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemService.getItems(null));
    }

    // -------------------------------------------------------------------
    // searchItems()
    // -------------------------------------------------------------------
    @Test
    void searchItems_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        when(itemRepository.searchItems("Item")).thenReturn(Arrays.asList(item));

        Collection<ItemDto> result = itemService.searchItems("Item");

        assertEquals(1, result.size());
        assertEquals("Item1", result.iterator().next().getName());
    }

    @Test
    void searchItems_emptyText() {
        Collection<ItemDto> result = itemService.searchItems("");
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------
    // addComment()
    // -------------------------------------------------------------------
    @Test
    void addComment_success() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("Nice item");
        commentDto.setAuthor(owner);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setBooker(owner);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Nice item");
        comment.setAuthor(owner);
        comment.setCreated(LocalDateTime.now());

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(bookingRepository.getItemBookings(1)).thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addComment(1, 1, commentDto);

        assertNotNull(result);
        assertEquals("Nice item", result.getText());
    }
//
//    @Test
//    void addComment_noBooking() {
//        CommentDto commentDto = new CommentDto("Nice item", 1, 1);
//        Item item = new Item(1, "Item1", "Description", true, new User(
//                1, "Owner", "owner@test.com
//                "));
//                User author = new User(1, "User", "user@test.com
//                ");
//                when(itemRepository.findById(1)).thenReturn(Optional.of(item));
//        when(userRepository.findById(1)).thenReturn(Optional.of(author));
//        when(bookingRepository.getItemBookings(1)).thenReturn(Collections.emptyList());
//
//        assertThrows(ValidationException.class, () -> itemService.addComment(1, 1, commentDto));
//    }
//
//    @Test
//    void addComment_itemNotFound() {
//        CommentDto commentDto = new CommentDto("Nice item", 1, 1);
//        when(itemRepository.findById(1)).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> itemService.addComment(1, 1, commentDto));
//    }
//
//    @Test
//    void addComment_userNotFound() {
//        CommentDto commentDto = new CommentDto("Nice item", 1, 1);
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> itemService.addComment(1, 1, commentDto));
//    }
}