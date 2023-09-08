package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.dto.LastBookingDto;
import ru.practicum.shareit.booking.model.dto.NextBookingDto;
import ru.practicum.shareit.item.Comment.model.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
    private List<CommentDto> comments;
}
