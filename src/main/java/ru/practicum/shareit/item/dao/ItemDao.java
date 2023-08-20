package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item addNewItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(long itemId);

    List<Item> getItemsByUserId(long userId);

    List<Item> searchInDescription(String text);

    void deleteUserItems(long userId);
}
