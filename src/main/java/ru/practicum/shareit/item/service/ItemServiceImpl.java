package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
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
import java.util.Optional;
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
            item.setIsAvailable(itemDto.getAvailable());
        }

        log.debug("Sending to DAO updated item.");
        itemRepository.save(item);

        return getItemDtoById(itemId, userId);
    }

    @Override
    @Transactional
    public ItemDto getItemDtoById(long itemId, long userId) {
        Item item = getItemById(itemId);
        ItemDto itemDto = utils.convertToDto(item);

        if (item.getOwner().getId() == userId) {
            Optional<Booking> lastBooking = Optional.ofNullable
                    (bookingService.getLastBookingForItem(itemDto.getId()));
            Optional<Booking> nextBooking = Optional.ofNullable
                    (bookingService.getNextBookingForItem(itemDto.getId()));
            lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.convertToLastBookingDto(lastBooking.get())));
            nextBooking.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.convertToNextBookingDto(nextBooking.get())));
        }

        itemDto.setComments(
                commentRepository.findAllByItem_IdOrderByIdDesc(itemId).stream()
                        .map(ItemMapper::convertToDto)
                        .collect(Collectors.toList())
        );

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

        List<Item> items = itemRepository.findAllByOwner_IdOrderByIdAsc(userId);

        return items.stream()
                .map(ItemMapper::convertToDto)
                .peek(itemDto -> {
                    Optional<Booking> lastBooking = Optional.ofNullable
                            (bookingService.getLastBookingForItem(itemDto.getId()));
                    lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.convertToLastBookingDto(lastBooking.get())));
                })
                .peek(itemDto -> {
                    Optional<Booking> nextBooking = Optional.ofNullable
                            (bookingService.getNextBookingForItem(itemDto.getId()));
                    nextBooking.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.convertToNextBookingDto(nextBooking.get())));
                })
                .peek(itemDto -> itemDto.setComments(
                        commentRepository.findAllByItem_IdOrderByIdDesc(itemDto.getId()).stream()
                                .map(ItemMapper::convertToDto)
                                .collect(Collectors.toList())
                ))
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
                .filter(Item::getIsAvailable)
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        utils.checkIfUserRentedItem(userId, itemId);
        Item item = getItemById(itemId);
        Comment comment = utils.createComment(commentDto, userId, item);
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
