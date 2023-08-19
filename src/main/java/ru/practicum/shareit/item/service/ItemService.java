package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);
    ItemDto updateItem(long userId, ItemDto itemDto);
    ItemDto getItemById(long userId);
    void checkIsItemPresent(long itemId);
    List<ItemDto> getItemsByUserId(long userId);
    List<ItemDto> searchInDescription(String text);
    void deleteUserItems(long userId);
}
