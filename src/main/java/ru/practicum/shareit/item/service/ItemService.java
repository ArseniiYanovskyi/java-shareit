package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);
    void setItemIsAvailable(long itemId, boolean isAvailable);

    ItemDto getItemDtoById(long userId);
    Item getItemById (long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    List<ItemDto> searchInDescription(String text);

    void deleteUserItems(long userId);
}
