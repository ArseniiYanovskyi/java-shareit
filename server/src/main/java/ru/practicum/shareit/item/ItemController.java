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
    private final String httpHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(httpHeaderUserId) long userId,
                           @RequestBody ItemDto itemDto) {
        log.debug("Received request to add new Item from user {}.", userId);

        return itemService.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(httpHeaderUserId) long userId,
                                 @PathVariable(value = "itemId") long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.debug("Received request to add new comment from user {} to item {}.", userId, itemId);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(httpHeaderUserId) long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to update existed Item with id {} from user id {}.", itemId, userId);
        itemDto.setId(itemId);
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(httpHeaderUserId) long userId,
                           @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to get existed Item with id {}.", itemId);

        return itemService.getItemDtoById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(httpHeaderUserId) long userId,
                                      @RequestParam(value = "from", required = false) Integer from,
                                      @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get items list by user id {}.", userId);
        if (from != null && size != null) {
            return itemService.getItemsByUserIdPagination(userId, from, size);
        }
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItems(@RequestParam String text,
                                        @RequestParam(value = "from", required = false) Integer from,
                                        @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request for search items by description with text: \"{}\"", text);
        if (from != null && size != null) {
            return itemService.searchInDescriptionPagination(text, from, size);
        }
        return itemService.searchInDescription(text);
    }
}
