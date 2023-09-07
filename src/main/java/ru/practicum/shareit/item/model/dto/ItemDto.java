package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.item.Comment.model.Comment;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<Comment> comments;
}
