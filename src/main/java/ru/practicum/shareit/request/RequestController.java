package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.AddRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{id}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
        return requestService.getRequest(userId, id);
    }

    @PostMapping
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody @Valid AddRequestDto addRequestDto) {
        return requestService.addRequest(userId, addRequestDto);
    }

    @GetMapping
    public Collection<RequestDto> getCurrentUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<RequestDto> getOtherUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getOtherUserRequests(userId);
    }

}
