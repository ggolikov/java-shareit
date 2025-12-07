package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Request mapToRequest(final RequestDto requestDto) {
        Request request = new Request();

        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setCreated(requestDto.getCreated());
        List<ItemDto> items = requestDto.getItems();

        if (items == null) {
            items = new ArrayList<>();
        }

        request.setItems(items.stream().map(ItemMapper::mapToItem).collect(Collectors.toList()));

        return request;
    }
}
