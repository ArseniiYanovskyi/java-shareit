package ru.practicum.shareit.request.service.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestServiceUtils {
    private final UserService userService;
    private final ItemService itemService;

    public ItemRequest checkAndConvertToRequest(long userId, ItemRequestDto itemRequestDto) {
        checkIsUserPresent(userId);
        //checkItemRequestDescription(itemRequestDto);
        return ItemRequestMapper.convertToRequest(itemRequestDto, userId);
    }

    public ItemRequestDto convertToDto(ItemRequest itemRequest) {
        List<ItemDto> items = getRequestAnswers(itemRequest.getId());
        if (items == null || items.size() < 1) {
            items = new ArrayList<>();
        }
        return ItemRequestMapper.convertToDto(itemRequest, items);
    }

    public void checkIsUserPresent(long userId) {
        userService.checkIsUserPresent(userId);
    }

    private List<ItemDto> getRequestAnswers(long requestId) {
        return itemService.getItemsForRequest(requestId);
    }

    private void checkItemRequestDescription(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null
                || itemRequestDto.getDescription().isBlank()) {
            throw new ValidationException("Request description can not be empty.");
        }
    }
}
