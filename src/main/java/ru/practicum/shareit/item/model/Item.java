package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class Item {
    private long itemId;
    private long ownerId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private boolean isAvailable;
}
