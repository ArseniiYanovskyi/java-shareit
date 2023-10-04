package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewRequest(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                                @RequestBody ItemRequestDto itemRequestDto) {
        log.debug("Received request to add new ItemRequest.");

        return requestClient.addNewRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersRequests(@RequestHeader(HTTP_HEADER_USER_ID) long userId) {
        log.debug("Received request to get user {} request list.", userId);

        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersExistingRequestsPagination
            (@RequestHeader(HTTP_HEADER_USER_ID) long userId,
             @RequestParam(value = "from", required = false) Integer from,
             @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Received request from user {} to get other users ItemsRequests.", userId);

        if (from != null && size != null) {
            return requestClient.getOtherUsersRequestsPagination(userId, from, size);
        }

        return requestClient.getOtherUsersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(HTTP_HEADER_USER_ID) long userId,
                                             @PathVariable(value = "requestId") long requestId) {
        log.debug("Received request to get request {}.", requestId);

        return requestClient.getRequest(userId, requestId);
    }
}
