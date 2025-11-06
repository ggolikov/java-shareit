package ru.practicum.shareit.item.repository;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemInMemoryRepository implements ItemRepository {
    private final Map<Integer, Item> items;

    public ItemInMemoryRepository() {
        items = new HashMap<>();
    }

    public Item getItem(Integer id) {
        if (!items.containsKey(id)) {
            throw new ValidationException("Вещь с id " + id + "не найдена");
        }
        return items.get(id);
    }

    public Item addItem(Item item) {
        validate(item);

        if (item.getId() == null) {
            item.setId(getNextId());
        }

        items.put(item.getId(), item);

        return item;
    }

    public Item updateItem(Item item) {
        validate(item);

        items.put(item.getId(), item);

        return item;
    }

    public void removeItem(Integer id) {
        items.remove(id);
    }

    public Collection<Item> getItems(Integer userId) {
        return items.values().stream().filter(item -> Objects.equals(item.getOwner().getId(), userId)).collect(Collectors.toList());
    }

    public Collection<Item> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return items.values().stream().filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()).collect(Collectors.toList());
    }

    public void validate(Item item) throws ValidationException {
        if (item.getAvailable() == null) {
            throw new ValidationException("Поле available не может быть null");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым или null");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Поле description не может быть пустым или null");
        }
    }

    private Integer getNextId() {
        int currentMaxId = items.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

