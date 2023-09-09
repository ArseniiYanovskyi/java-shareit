package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private long id;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private String status;
}
