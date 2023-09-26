package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getUserRequests(long userId);

    List<ItemRequestDto> getOtherUsersRequests(long userId);

    List<ItemRequestDto> getOtherUsersRequestsPagination(long userId, int from, int size);

    ItemRequestDto getRequest(long userId, long requestId);
}
