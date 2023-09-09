package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDto getItemDtoById(long itemId, long userId);

    Item getItemById(long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    List<ItemDto> searchInDescription(String text);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

    void deleteUserItems(long userId);
}
