package ru.practicum.shareit.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.request.dto.AddRequestDto;
import ru.practicum.shareit.server.request.dto.RequestDto;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.repository.RequestRepository;
import ru.practicum.shareit.server.request.service.RequestServiceImpl;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceImplTest {

    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        requestRepository = mock(RequestRepository.class);
        userRepository = mock(UserRepository.class);
        requestService = new RequestServiceImpl(requestRepository, userRepository);
    }

    // -------------------------------------------------------------------
    // getRequest()
    // -------------------------------------------------------------------
    @Test
    void getRequest_success() {
        Integer userId = 1;
        Integer requestId = 1;

        Request request = new Request();
        request.setId(requestId);
        request.setDescription("Request Description");
        request.setCreated(LocalDateTime.now());

        RequestDto expectedDto = new RequestDto();
        expectedDto.setId(requestId);
        expectedDto.setDescription("Request Description");
        expectedDto.setCreated(LocalDateTime.now());

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);

        RequestDto result = requestService.getRequest(userId, requestId);

        assertNotNull(result);
        assertEquals(expectedDto.getDescription(), result.getDescription());
        verify(requestRepository).findById(requestId);
    }

    @Test
    void getRequest_userNotFound() {
        Integer userId = 1;
        Integer requestId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequest(userId, requestId));
    }

    @Test
    void getRequest_requestNotFound() {
        Integer userId = 1;
        Integer requestId = 1;

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequest(userId, requestId));
    }

    // -------------------------------------------------------------------
    // addRequest()
    // -------------------------------------------------------------------
    @Test
    void addRequest_success() {
        Integer userId = 1;
        AddRequestDto addRequestDto = new AddRequestDto();
        addRequestDto.setDescription("Request Description");

        Request savedRequest = new Request();
        savedRequest.setId(1);
        savedRequest.setDescription("Request Description");
        savedRequest.setCreated(LocalDateTime.now());

        RequestDto expectedDto = new RequestDto();
        expectedDto.setId(1);
        expectedDto.setDescription("Request Description");
        expectedDto.setCreated(LocalDateTime.now());

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);

        RequestDto result = requestService.addRequest(userId, addRequestDto);

        assertNotNull(result);
        assertEquals(expectedDto.getDescription(), result.getDescription());
        verify(requestRepository).save(any(Request.class));
    }

    // -------------------------------------------------------------------
    // getUserRequests()
    // -------------------------------------------------------------------
    @Test
    void getUserRequests_success() {
        Integer userId = 1;
        Request request = new Request();
        request.setId(1);
        request.setDescription("Request Description");
        request.setCreated(LocalDateTime.now());

        RequestDto requestDto = new RequestDto();
        requestDto.setId(1);
        requestDto.setDescription("Request Description");
        requestDto.setCreated(LocalDateTime.now());

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.getUserRequests(userId)).thenReturn(Arrays.asList(request));

        Collection<RequestDto> result = requestService.getUserRequests(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(requestDto.getDescription(), result.iterator().next().getDescription());
        verify(requestRepository).getUserRequests(userId);
    }

    @Test
    void getUserRequests_userNotFound() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getUserRequests(userId));
    }

    // -------------------------------------------------------------------
    // getOtherUserRequests()
    // -------------------------------------------------------------------
    @Test
    void getOtherUserRequests_success() {
        Integer userId = 1;
        Request request = new Request();
        request.setId(1);
        request.setDescription("Request Description");
        request.setCreated(LocalDateTime.now());

        RequestDto requestDto = new RequestDto();
        requestDto.setId(1);
        requestDto.setDescription("Request Description");
        requestDto.setCreated(LocalDateTime.now());

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.getOtherUserRequests(userId)).thenReturn(Arrays.asList(request));

        Collection<RequestDto> result = requestService.getOtherUserRequests(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(requestDto.getDescription(), result.iterator().next().getDescription());
        verify(requestRepository).getOtherUserRequests(userId);
    }

    @Test
    void getOtherUserRequests_userNotFound() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getOtherUserRequests(userId));
    }
}