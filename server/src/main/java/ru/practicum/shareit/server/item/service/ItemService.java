package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.dto.AddItemDto;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ExtendedItemDto;
import ru.practicum.shareit.server.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ExtendedItemDto getItem(Integer id);

    ItemDto addItem(Integer userId, AddItemDto addItemDto);

    ItemDto updateItem(Integer id, Integer userId, ItemDto itemDto);

    Collection<ExtendedItemDto> getItems(Integer userId);

    Collection<ItemDto> searchItems(String text);

    CommentDto addComment(Integer id, Integer userId, CommentDto commentDto);
}
