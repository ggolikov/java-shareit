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
import ru.practicum.shareit.server.item.ItemController;
import ru.practicum.shareit.server.item.dto.AddItemDto;
import ru.practicum.shareit.server.item.dto.ExtendedItemDto;
import ru.practicum.shareit.server.item.service.ItemService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getItem() throws Exception {
        ExtendedItemDto item = new ExtendedItemDto();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        Mockito.when(itemService.getItem(Mockito.anyInt()))
                .thenReturn(item);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    void addItem() throws Exception {
        AddItemDto item = new AddItemDto();
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        ExtendedItemDto returnedItem = new ExtendedItemDto();
        returnedItem.setId(1);
        item.setName(item.getName());
        item.setDescription(item.getDescription());
        item.setAvailable(item.getAvailable());

        Mockito.when(itemService.addItem(1, item))
                .thenReturn(returnedItem);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedItem.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(returnedItem.getName())))
                .andExpect(jsonPath("$.description", is(returnedItem.getDescription())))
                .andExpect(jsonPath("$.available", is(returnedItem.getAvailable())));
    }
}