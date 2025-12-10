package ru.practicum.shareit.server.request.service;

import ru.practicum.shareit.server.request.dto.AddRequestDto;
import ru.practicum.shareit.server.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {
    RequestDto getRequest(Integer userId, Integer requestId);

    RequestDto addRequest(Integer userId, AddRequestDto addRequestDto);

    Collection<RequestDto> getUserRequests(Integer userId);

    Collection<RequestDto> getOtherUserRequests(Integer userId);
}
