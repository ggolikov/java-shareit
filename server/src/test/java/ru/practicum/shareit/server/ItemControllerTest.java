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
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ExtendedItemDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void updateItem() throws Exception {
        ExtendedItemDto returnedItem = new ExtendedItemDto();
        returnedItem.setId(1);
        returnedItem.setName("Updated Item");
        returnedItem.setDescription("Updated Description");
        returnedItem.setAvailable(true);

        ItemDto updatedItem = new ItemDto();
        updatedItem.setId(returnedItem.getId());
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");

        Mockito.when(itemService.updateItem(1, returnedItem.getId(), updatedItem))
                .thenReturn(returnedItem);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(updatedItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedItem.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(returnedItem.getName())))
                .andExpect(jsonPath("$.description", is(returnedItem.getDescription())))
                .andExpect(jsonPath("$.available", is(returnedItem.getAvailable())));
    }

    @Test
    void getItems() throws Exception {
        ExtendedItemDto item = new ExtendedItemDto();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        Mockito.when(itemService.getItems(Mockito.anyInt()))
                .thenReturn(List.of(item));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchItems() throws Exception {
        ExtendedItemDto item = new ExtendedItemDto();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        Mockito.when(itemService.searchItems(Mockito.anyString()))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search?text=Item")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void addComment() throws Exception {
        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        CommentDto comment = new CommentDto();
        comment.setId(1);
        comment.setText("Comment");
        comment.setItem(item);

        Mockito.when(itemService.addComment(1, 1, comment))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}