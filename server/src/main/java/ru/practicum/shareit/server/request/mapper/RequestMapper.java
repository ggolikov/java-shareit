package ru.practicum.shareit.server.request.mapper;

import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.dto.RequestDto;
import ru.practicum.shareit.server.request.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    public static RequestDto mapToRequestDto(final Request request) {
        RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        List<Item> items = request.getItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        requestDto.setItems(items.stream().map(ItemMapper::mapToItemDto).toList());

        return requestDto;
    }
}
