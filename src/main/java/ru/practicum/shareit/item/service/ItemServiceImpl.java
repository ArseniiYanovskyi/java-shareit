package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.item.Comment.CommentRepository;
import ru.practicum.shareit.item.Comment.model.Comment;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.utils.ItemServiceUtils;
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
    private final BookingService bookingService;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemServiceUtils utils;

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        utils.checkItemDtoValidation(itemDto);
        utils.checkIsItemAvailable(itemDto);

        User owner = UserMapper.convertToUser(userService.getUserDtoById(userId));

        Item item = utils.convertToItem(itemDto, owner);

        log.debug("Sending to DAO item to create with name {} and description {} from user {}.",
                item.getName(), item.getDescription(), userId);

        return utils.convertToDto(itemRepository.save(item));
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
        ItemDto itemDto = utils.convertToDto(getItemById(itemId));
        itemDto.setComments(commentRepository.findAllByItem_IdOrderByIdDesc(itemId));
        return itemDto;
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
                .peek(itemDto -> {
                    BookingDto lastBooking = bookingService.getLastBookingForItem(itemDto.getId());
                    if (lastBooking != null) {
                        itemDto.setLastBooking(lastBooking);
                    }
                })
                .peek(itemDto -> {
                    BookingDto nextBooking = bookingService.getNextBookingForItem(itemDto.getId());
                    if (nextBooking != null) {
                        itemDto.setNextBooking(nextBooking);
                    }
                })
                .peek(itemDto -> itemDto.setComments(commentRepository.findAllByItem_Owner_IdOrderByIdDesc(userId)))
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
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        utils.checkIfUserRentedItem(userId, itemId);
        Comment comment = utils.createComment(commentDto, userId, getItemById(itemId));
        log.debug("Sending to DAO request to add new comment from user {} to item {}.", userId, itemId);

        return utils.convertToDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteUserItems(long userId) {
        //проверка наличия пользователя отсутствует потому что метод вызывается после удаления пользователя
        log.debug("Sending to DAO request to delete user id {} items.", userId);
        itemRepository.deleteById(userId);
    }
}
