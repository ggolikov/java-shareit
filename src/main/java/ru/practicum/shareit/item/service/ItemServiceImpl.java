package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("itemService")
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public ExtendedItemDto getItem(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        ExtendedItemDto itemDto = ItemMapper.mapToExtendedItemDto(item);

        Collection<Booking> itemBookings = bookingRepository.getItemBookings(id);
        LocalDateTime now = LocalDateTime.now();

        Booking lastBooking = itemBookings.stream().filter(b -> b.getEnd().isAfter(now)).findFirst().orElse(null);
        Booking nextBooking = itemBookings.stream().filter(b -> b.getStart().isAfter(now)).findFirst().orElse(null);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        Collection<Comment> comments = commentRepository.getItemComments(id);

        itemDto.setComments(comments.stream().map(CommentMapper::mapToCommentDto).toList());

        return itemDto;
    }

    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ValidationException("Name is empty");
        }

        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        itemDto.setOwner(owner);
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto));

        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Integer id, Integer userId, ItemDto itemDto) {
        Item updatedItem = ItemMapper.mapToItem(itemDto);
        Item item = ItemMapper.mapToItem(getItem(id));

        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Item existingItem = new Item();
        existingItem.setId(item.getId());
        existingItem.setOwner(owner);
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setAvailable(item.getAvailable());

        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }

        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }

        if (updatedItem.getAvailable() != null) {
            existingItem.setAvailable(updatedItem.getAvailable());
        }

        return ItemMapper.mapToItemDto(itemRepository.save(existingItem));
    }

    @Transactional(readOnly = true)
    public Collection<ExtendedItemDto> getItems(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Collection<ExtendedItemDto> items = itemRepository.findAll().stream().filter(item -> Objects.equals(item.getOwner().getId(), userId)).map(ItemMapper::mapToExtendedItemDto).collect(Collectors.toList());

        for (ExtendedItemDto itemDto : items) {
            Integer itemId = itemDto.getId();

            Collection<Booking> itemBookings = bookingRepository.getItemBookings(itemId);
            LocalDateTime now = LocalDateTime.now();

            Booking lastBooking = itemBookings.stream().filter(b -> b.getEnd().isAfter(now)).findFirst().orElse(null);
            Booking nextBooking = itemBookings.stream().filter(b -> b.getStart().isAfter(now)).findFirst().orElse(null);

            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }

        return items;
    }

    @Transactional(readOnly = true)
    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.searchItems(text).stream().map(ItemMapper::mapToItemDto)
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()).collect(Collectors.toList());
    }

    public CommentDto addComment(Integer id, Integer userId, CommentDto commentDto) {
        LocalDateTime now = LocalDateTime.now();

        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Collection<Booking> itemBookingsFromUser = bookingRepository.getItemBookings(item.getId()).stream().filter(booking -> Objects.equals(booking.getBooker().getId(), userId) && booking.getEnd().isBefore(now)).collect(Collectors.toList());

        if (itemBookingsFromUser.isEmpty()) {
            throw new ValidationException("Comment from not an item owner");
        }

        commentDto.setItem(item);
        commentDto.setAuthor(author);
        commentDto.setCreated(now);

        Comment comment = commentRepository.save(CommentMapper.mapToComment(commentDto));

        return CommentMapper.mapToCommentDto(comment);
    }
}
