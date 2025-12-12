package ru.practicum.shareit.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.request.RequestController;
import ru.practicum.shareit.server.request.dto.AddRequestDto;
import ru.practicum.shareit.server.request.dto.RequestDto;
import ru.practicum.shareit.server.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getRequest() throws Exception {
        RequestDto request = new RequestDto();
        request.setId(1);
        request.setDescription("description");

        Mockito.when(requestService.getRequest(1, request.getId()))
                .thenReturn(request);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    @Test
    void addRequest() throws Exception {
        AddRequestDto addedRequest = new AddRequestDto();
        addedRequest.setDescription("description");

        RequestDto request = new RequestDto();

        request.setId(1);
        request.setDescription(addedRequest.getDescription());

        Mockito.when(requestService.addRequest(1, addedRequest))
                .thenReturn(request);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(addedRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    @Test
    void getCurrentUserRequests() throws Exception {
        RequestDto request = new RequestDto();
        request.setDescription("description");
        request.setId(1);

        Mockito.when(requestService.getUserRequests(1))
                .thenReturn(List.of(request));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getOtherUserRequests() throws Exception {
        RequestDto request = new RequestDto();
        request.setDescription("description");
        request.setId(1);

        Mockito.when(requestService.getOtherUserRequests(1))
                .thenReturn(List.of(request));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}