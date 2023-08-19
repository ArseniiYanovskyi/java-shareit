package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item addNewItem(Item item);
    Item updateItemName(long userId, long itemId, String name);
    Item updateItemNDescription(long userId, long itemId, String description);
    Item updateItemAvailability(long userId, long itemId, Boolean isAvailable);
    Optional<Item> getItemById(long itemId);
    List<Long> getIdOfUsersItems(long userId);
    List<Item> getItemsByUserId(long userId);
    List<Item> searchInDescription(String text);
    void deleteUserItems(long userId);
}
