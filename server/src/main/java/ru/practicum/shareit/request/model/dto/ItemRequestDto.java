package ru.practicum.shareit.request.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
