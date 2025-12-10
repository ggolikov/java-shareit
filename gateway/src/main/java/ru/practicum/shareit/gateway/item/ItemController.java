package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.service.ItemClient;
import ru.practicum.shareit.gateway.item.dto.AddItemDto;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Long id) {
        return client.getItem(id);
    }

    @PostMapping()
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody AddItemDto addItemDto) {
        addItemDto.setOwnerId(userId);
        return client.addItem(userId, addItemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        return client.updateItem(id, userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("text") String text) {
        return client.searchItems(userId, text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody CommentDto commentDto) {
        return client.addComment(id, userId, commentDto);
    }
}
