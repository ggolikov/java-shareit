package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ExtendedItemDto getItem(Integer id);

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer id, Integer userId, ItemDto itemDto);

    Collection<ExtendedItemDto> getItems(Integer userId);

    Collection<ItemDto> searchItems(String text);

    CommentDto addComment(Integer id, Integer userId, CommentDto commentDto);
}
