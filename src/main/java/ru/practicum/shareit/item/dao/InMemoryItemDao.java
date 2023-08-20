package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InMemoryItemDao implements ItemDao {
    private final HashMap<Long, Item> items;
    private long itemIdCounter = 1;

    @Override
    public Item addNewItem(Item item) {
        log.debug("Received item to add as new one.");

        long newItemId = generateId();

        item.setItemId(newItemId);

        items.put(newItemId, item);

        log.debug("New item with id {} added successfully.", newItemId);

        return getItemById(newItemId).get();
    }

    @Override
    public Item updateItem(Item item) {
        log.debug("Received updated item with id {}.", item.getItemId());

        items.put(item.getItemId(), item);

        return getItemById(item.getItemId()).get();
    }

    @Override
    public Optional<Item> getItemById(long id) {
        log.debug("Received request to get item with id {}", id);

        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        log.debug("Received request to get all items own by user with id {}", userId);

        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchInDescription(String text) {
        log.debug("Received request to search items which description contains text \"{}\"", text);

        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserItems(long userId) {
        log.debug("Received request to delete user with id {} items", userId);
        items.remove(userId);
    }

    private Long generateId() {
        return itemIdCounter++;
    }
}
