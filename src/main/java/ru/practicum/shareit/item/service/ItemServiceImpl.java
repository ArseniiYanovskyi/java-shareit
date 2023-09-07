package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkItemDtoValidation(itemDto);
        checkIsItemAvailable(itemDto);

        User owner = UserMapper.convertToUser(userService.getUserDtoById(userId));

        Item item = convertToItem(itemDto, owner);

        log.debug("Sending to DAO item to create with name {} and description {} from user {}.",
                item.getName(), item.getDescription(), userId);

        return convertToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        userService.checkIsUserPresent(userId);

        long itemId = itemDto.getId();
        Item item = getItemById(itemId);

        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Information about this user's item absent.");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        log.debug("Sending to DAO updated item.");
        itemRepository.save(item);

        return getItemDtoById(itemId);
    }

    @Override
    @Transactional
    public void setItemIsAvailable(long itemId, boolean isAvailable) {
        Item item = getItemById(itemId);
        item.setAvailable(isAvailable);
        log.debug("Sending to DAO updated item {}.(is available = {})", itemId, isAvailable);
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public ItemDto getItemDtoById(long itemId) {
        return convertToDto(getItemById(itemId));
    }

    @Override
    @Transactional
    public Item getItemById(long itemId) {
        log.debug("Sending to DAO request to get item with id {}.", itemId);
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not present in repository."));
    }

    @Override
    @Transactional
    public List<ItemDto> getItemsByUserId(long userId) {
        userService.checkIsUserPresent(userId);

        log.debug("Sending to DAO request for get items by user id {}.", userId);

        List<Item> items = itemRepository.findAllByOwner_Id(userId);

        return items.stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemDto> searchInDescription(String text) {
        log.debug("Sending to DAO request to search items by text \"{}\".", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.findAllByDescriptionContainsIgnoreCase(text);

        return items.stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserItems(long userId) {
        //проверка наличия пользователя отсутствует потому что метод вызывается после удаления пользователя
        log.debug("Sending to DAO request to delete user id {} items.", userId);
        itemRepository.deleteById(userId);
    }

    private void checkItemDtoValidation(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Name is blank.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Description is blank.");
        }
    }

    private void checkIsItemAvailable(ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Item is not available.");
        }
    }

    private ItemDto convertToDto(Item item) {
        return ItemMapper.convertToDto(item);
    }

    private Item convertToItem(ItemDto itemDto, User owner) {
        return ItemMapper.convertToItem(itemDto, owner);
    }

    private void checkIsItemPresent(long itemId) {
        Item item = itemRepository.getReferenceById(itemId);
        if (item.getName() == null || item.getDescription() == null || item.getOwner() == null) {
            throw new NotFoundException("Item with id " + itemId + " does not present in repository.");
        }
    }
}
