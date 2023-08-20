package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkItemDtoValidation(itemDto);
        checkIsItemAvailable(itemDto);
        userService.checkIsUserPresent(userId);

        Item item = convertToItem(userId, itemDto);

        log.debug("Sending to DAO item to create with name {} and description {} from user {}.",
                item.getName(), item.getDescription(), userId);

        return convertToDto(itemRepository.addNewItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        userService.checkIsUserPresent(userId);

        long itemId = itemDto.getId();
        Item item = getItemById(itemId);

        if (item.getOwnerId() != userId) {
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

        log.debug("Sending to DAO updated item");
        itemRepository.updateItem(item);

        return getItemDtoById(itemId);
    }


    @Override
    public ItemDto getItemDtoById(long itemId) {
        log.debug("Sending to DAO request to get item with id {}.", itemId);
        return convertToDto(itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not present in repository.")));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        userService.checkIsUserPresent(userId);

        log.debug("Sending to DAO request for get items by user id {}.", userId);

        List<Item> items = itemRepository.getItemsByUserId(userId);

        return items.stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchInDescription(String text) {
        log.debug("Sending to DAO request to search items by text \"{}\".", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchInDescription(text);

        return items.stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserItems(long userId) {
        //проверка наличия пользователя отсутствует потому что метод вызывается после удаления пользователя
        log.debug("Sending to DAO request to delete user id {} items.", userId);
        itemRepository.deleteUserItems(userId);
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

    private Item getItemById(long itemId) {
        log.debug("Sending to DAO request to get item with id {}.", itemId);
        return itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not present in repository."));
    }

    private ItemDto convertToDto(Item item) {
        return ItemMapper.convertToDto(item);
    }

    private Item convertToItem(long userId, ItemDto itemDto) {
        return ItemMapper.convertToItem(userId, itemDto);
    }

    private void checkIsItemPresent(long itemId) {
        itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not present in repository."));
    }
}
