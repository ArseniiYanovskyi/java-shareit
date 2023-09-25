package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest convertToRequest(ItemRequestDto itemRequestDto, long publisher) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setPublisher(publisher);
        itemRequest.setCreationDate(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto convertToDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreationDate())
                .items(items)
                .build();
    }
}
