package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("itemService")
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto getItem(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ValidationException("Name is empty");
        }

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        itemDto.setOwnerId(userId);
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
        existingItem.setOwnerId(owner.getId());
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

    public Collection<ItemDto> getItems(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        return itemRepository.findAll().stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.searchItems(text).stream().map(ItemMapper::mapToItemDto)
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()).collect(Collectors.toList());
    }
}
