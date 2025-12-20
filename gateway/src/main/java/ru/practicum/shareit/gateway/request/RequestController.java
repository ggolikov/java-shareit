package ru.practicum.shareit.gateway.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.service.RequestClient;
import ru.practicum.shareit.gateway.request.dto.AddRequestDto;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Integer id) {
        return client.getRequest(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid AddRequestDto addRequestDto) {
        return client.addRequest(userId, addRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getCurrentUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.getOtherUserRequests(userId);
    }
}
