package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                           @RequestBody ItemDto itemDto) {
        log.debug("Received request to add new Item from user {}.", userId);

        return itemService.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                 @PathVariable(value = "itemId") long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.debug("Received request to add new comment from user {} to item {}.", userId, itemId);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to update existed Item with id {} from user id {}.", itemId, userId);
        itemDto.setId(itemId);
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                           @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to get existed Item with id {}.", itemId);

        return itemService.getItemDtoById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(HTTP_HEADER_USER_ID) long userId) {
        log.debug("Received request to get items list by user id {}.", userId);

        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItems(@RequestParam String text) {
        log.debug("Received request for search items by description with text: \"{}\"", text);

        return itemService.searchInDescription(text);
    }

}
