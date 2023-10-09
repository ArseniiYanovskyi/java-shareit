package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_IdOrderByIdAsc(long ownerId);

    Page<Item> findAllByOwner_IdOrderByIdAsc(long ownerId, Pageable page);

    List<Item> findAllByDescriptionContainsIgnoreCase(String description);

    Page<Item> findAllByDescriptionContainsIgnoreCase(String description, Pageable page);

    List<Item> findAllByRequest(long request);
}
