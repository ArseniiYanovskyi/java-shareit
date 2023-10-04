package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private final String httpHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(httpHeaderUserId) long userId,
                                          @RequestBody ItemDto itemDto) {
        log.debug("Received request to add new Item from user {}.", userId);

        return itemClient.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(httpHeaderUserId) long userId,
                                             @PathVariable(value = "itemId") long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.debug("Received request to add new comment from user {} to item {}.", userId, itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(httpHeaderUserId) long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to update existed Item with id {} from user id {}.", itemId, userId);
        itemDto.setId(itemId);
        return itemClient.updateItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(httpHeaderUserId) long userId,
                                          @PathVariable(value = "itemId") long itemId) {
        log.debug("Received request to get existed Item with id {}.", itemId);

        return itemClient.getItemDtoById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(httpHeaderUserId) long userId,
                                               @RequestParam(value = "from", required = false) Integer from,
                                               @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request to get items list by user id {}.", userId);
        if (from != null && size != null) {
            return itemClient.getItemsByUserIdPagination(userId, from, size);
        }
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchForItems(@RequestParam String text,
                                                 @RequestParam(value = "from", required = false) Integer from,
                                                 @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request for search items by description with text: \"{}\"", text);
        if (from != null && size != null) {
            return itemClient.searchInDescriptionPagination(text, from, size);
        }
        return itemClient.searchInDescription(text);
    }
}
