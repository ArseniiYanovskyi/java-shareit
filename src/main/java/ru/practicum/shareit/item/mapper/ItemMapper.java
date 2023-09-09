package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.Comment.model.Comment;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static Item convertToItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        if (itemDto.getId() != 0) {
            item.setId(itemDto.getId());
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        item.setIsAvailable(itemDto.getAvailable());
        if (owner != null) {
            item.setOwner(owner);
        }
        return item;
    }

    public static ItemDto convertToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .build();
    }

    public static CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .itemName(comment.getItem().getName())
                .created(comment.getCreationDate())
                .build();
    }
}
