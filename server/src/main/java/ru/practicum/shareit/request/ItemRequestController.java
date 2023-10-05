package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService requestService;

    private final String httpHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader(httpHeaderUserId) long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Received request to add new ItemRequest.");

        return requestService.addNewRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUsersRequests(@RequestHeader(httpHeaderUserId) long userId) {
        log.info("Received request to get user {} request list.", userId);

        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherUsersExistingRequestsPagination(@RequestHeader(httpHeaderUserId) long userId,
             @RequestParam(value = "from", required = false) Integer from,
             @RequestParam(value = "size", required = false) Integer size) {
        log.info("Received request from user {} to get other users ItemsRequests.", userId);

        if (from != null && size != null) {
            return requestService.getOtherUsersRequestsPagination(userId, from, size);
        }

        return requestService.getOtherUsersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(httpHeaderUserId) long userId,
                                     @PathVariable(value = "requestId") long requestId) {
        log.info("Received request to get request {}.", requestId);

        return requestService.getRequest(userId, requestId);
    }
}