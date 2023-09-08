package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_IdOrderByIdAsc(long ownerId);

    List<Item> findAllByDescriptionContainsIgnoreCase(String description);
}
