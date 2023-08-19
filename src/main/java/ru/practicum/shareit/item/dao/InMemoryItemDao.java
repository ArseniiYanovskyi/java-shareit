package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryItemDao implements ItemDao{
    private final HashMap<Long, HashMap<Long, Item>> items;
    private long itemIdCounter = 1;
    private final Logger log = LoggerFactory.getLogger("ItemRepository");
    @Override
    public Item addNewItem(Item item) {
        log.debug("Received item to add as new one.");

        long itemOwnerId = item.getOwnerId();
        long newItemId = itemIdCounter++;

        item.setItemId(newItemId);

        if (items.containsKey(item.getOwnerId())) {
            items.get(itemOwnerId).put(newItemId, item);
        } else {
            items.put(itemOwnerId, new HashMap<>());
            items.get(itemOwnerId).put(newItemId, item);
        }

        log.debug("New item with id {} added successfully.", newItemId);

        return getItemById(newItemId).get();
    }

    @Override
    public Item updateItemName(long userId, long itemId, String name) {
        log.debug("Received new name for item id {}.", itemId);

        Item item = getItemById(itemId).get();
        item.setName(name);

        HashMap<Long, Item> userItems = items.get(userId);
        userItems.put(itemId, item);

        items.put(userId, userItems);

        log.debug("Item name with id {} updated successfully.", itemId);
        return getItemById(itemId).get();
    }
    @Override
    public  Item updateItemNDescription(long userId, long itemId, String description) {
        log.debug("Received new name for item id {}.", itemId);

        Item item = getItemById(itemId).get();
        item.setDescription(description);

        HashMap<Long, Item> userItems = items.get(userId);
        userItems.put(itemId, item);

        items.put(userId, userItems);

        log.debug("Item name with id {} updated successfully.", itemId);
        return items.get(userId).get(itemId);
    }
    @Override
    public Item updateItemAvailability(long userId, long itemId, Boolean isAvailable) {
        log.debug("Received new name for item id {}.", itemId);
        Item item = getItemById(itemId).get();
        item.setAvailable(isAvailable);

        HashMap<Long, Item> userItems = items.get(userId);
        userItems.put(itemId, item);

        items.put(userId, userItems);

        log.debug("Item name with id {} updated successfully.", itemId);
        return getItemById(itemId).get();
    }

    @Override
    public Optional<Item> getItemById(long id) {
        log.debug("Received request to get item with id {}", id);

        return items.values().stream()
                .filter(userMap -> userMap.containsKey(id))
                .map(userMap -> userMap.get(id))
                .findFirst();
    }

    @Override
    public List<Long> getIdOfUsersItems(long userId) {
        log.debug("Received request to get all items ID's own by user with id {}", userId);
        if (items.containsKey(userId)) {
            return new ArrayList<>(items.get(userId).keySet());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        log.debug("Received request to get all items own by user with id {}", userId);

        return new ArrayList<>(items.get(userId).values());
    }

    @Override
    public List<Item> searchInDescription(String text) {
        log.debug("Received request to search items which description contains text \"{}\"", text);

        ArrayList<Item> returningList = new ArrayList<>();

        for (Long userId : items.keySet()) {
            for (Long itemId : items.get(userId).keySet()) {
                if (items.get(userId).get(itemId).getDescription().toUpperCase().contains(text.toUpperCase())
                    && items.get(userId).get(itemId).isAvailable()) {
                    returningList.add(items.get(userId).get(itemId));
                }
            }
        }

        return returningList;
    }

    @Override
    public void deleteUserItems(long userId) {
        log.debug("Received request to delete user with id {} items", userId);
        items.remove(userId);
    }
}
