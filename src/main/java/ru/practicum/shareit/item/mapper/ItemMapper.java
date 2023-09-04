package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static Item convertToItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        if (itemDto.getId() != 0) {
            item.setId(itemDto.getId());
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (!itemDto.getAvailable().booleanValue()) {
            item.setAvailable(false);
        } else {
            item.setAvailable(true);
        }
        if (owner != null) {
            item.setOwner(owner);
        }
        return item;
    }

    public static ItemDto convertToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }
}
