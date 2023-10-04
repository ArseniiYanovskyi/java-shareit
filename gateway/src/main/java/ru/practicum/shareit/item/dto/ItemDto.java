package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import src.main.java.ru.practicum.shareit.booking.dto.BookingLink;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLink lastBooking;
    private BookingLink nextBooking;
    private List<CommentDto> comments;
    private long requestId;
}
