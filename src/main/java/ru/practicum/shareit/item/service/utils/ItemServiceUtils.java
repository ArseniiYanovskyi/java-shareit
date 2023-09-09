package ru.practicum.shareit.item.service.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.Comment.model.Comment;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemServiceUtils {
    private final BookingService bookingService;
    private final UserService userService;

    public void checkItemDtoValidation(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Name is blank.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Description is blank.");
        }
    }

    public void checkIsItemAvailable(ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Item is not available.");
        }
    }

    public ItemDto convertToDto(Item item) {
        return ItemMapper.convertToDto(item);
    }

    public Item convertToItem(ItemDto itemDto, User owner) {
        return ItemMapper.convertToItem(itemDto, owner);
    }

    public void checkIfUserRentedItem(long userId, long itemId) {
        List<BookingDto> usersBookings = bookingService.getUsersBookings(userId, "ALL");
        if (usersBookings.stream()
                .filter(bookingDto -> bookingDto.getItem().getId() == itemId)
                .filter(bookingDto -> bookingDto.getStatus().equals("APPROVED"))
                .filter(bookingDto -> bookingDto.getStart().isBefore(LocalDateTime.now()))
                .count() == 0) {
            throw new ValidationException("This user wasn't rented item.");
        }
    }

    public Comment createComment(CommentDto commentDto, long userId, Item item) {
        Comment comment = new Comment();
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            throw new ValidationException("Comment text can not be empty.");
        } else {
            comment.setText(commentDto.getText());
        }
        User author = userService.getUserById(userId);

        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreationDate(LocalDateTime.now());

        return comment;
    }

    public CommentDto convertToDto(Comment comment) {
        return ItemMapper.convertToDto(comment);
    }

}
