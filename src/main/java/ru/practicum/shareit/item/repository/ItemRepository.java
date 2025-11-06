package ru.practicum.shareit.item.repository;

import jakarta.validation.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item getItem(Integer id);

    Item addItem(Item item);

    Item updateItem(Item item);

    void removeItem(Integer id);

    Collection<Item> getItems(Integer UserId);

    Collection<Item> searchItems(String text);

    void validate(Item item) throws ValidationException;
}
