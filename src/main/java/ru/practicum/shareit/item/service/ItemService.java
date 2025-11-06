package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto getItem(Integer id);

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer id, Integer userId, ItemDto itemDto);

    Collection<ItemDto> getItems(Integer userId);

    Collection<ItemDto> searchItems(String text);
}
