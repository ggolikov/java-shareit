package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("itemService")
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto getItem(Integer id) {
        return ItemMapper.mapToItemDto(itemRepository.getItem(id));
    }

    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        Item addedItem = ItemMapper.mapToItem(itemDto);

        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        User owner = userRepository.getUser(userId);

        if (owner == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }

        addedItem.setOwner(owner);

        return ItemMapper.mapToItemDto(itemRepository.addItem(addedItem));
    }

    public ItemDto updateItem(Integer id, Integer userId, ItemDto itemDto) {
        Item updatedItem = ItemMapper.mapToItem(itemDto);
        Item item = itemRepository.getItem(id);

        if (item == null) {
            return addItem(userId, itemDto);
        }

        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        User owner = userRepository.getUser(userId);

        if (owner == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }

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

        return ItemMapper.mapToItemDto(itemRepository.updateItem(existingItem));
    }

    public Collection<ItemDto> getItems(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Параметр userId не может быть null");
        }

        User user = userRepository.getUser(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }

        return itemRepository.getItems(userId).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }
}
