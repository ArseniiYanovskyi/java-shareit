package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item convertToItem(long ownerId, ItemDto itemDto) {
        return Item.builder()
                .itemId(itemDto.getId())
                .ownerId(ownerId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .build();
    }

    public static ItemDto convertToDto(Item item) {
        return ItemDto.builder()
                .id(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }
}
