package ru.practicum.shareit.item.Comment.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private long id;
    private String text;
    private String authorName;
    private String itemName;
    private LocalDateTime created;

}
