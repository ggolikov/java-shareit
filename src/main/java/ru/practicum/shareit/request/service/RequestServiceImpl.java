package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.AddRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("requestService")
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestDto getRequest(Integer userId, Integer requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));

        return RequestMapper.mapToRequestDto(request);
    }

    public RequestDto addRequest(Integer userId, AddRequestDto addRequestDto) {
        Request newRequest = new Request();
        LocalDateTime now = LocalDateTime.now();

        newRequest.setDescription(addRequestDto.getDescription());
        newRequest.setRequestorId(userId);
        newRequest.setCreated(now);

        Request request = requestRepository.save(newRequest);

        return RequestMapper.mapToRequestDto(request);
    }

    public Collection<RequestDto> getUserRequests(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return requestRepository.getUserRequests(userId).stream().map(RequestMapper::mapToRequestDto).collect(Collectors.toList());
    }

    public Collection<RequestDto> getOtherUserRequests(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return requestRepository.getOtherUserRequests(userId).stream().map(RequestMapper::mapToRequestDto).collect(Collectors.toList());
    }
}
