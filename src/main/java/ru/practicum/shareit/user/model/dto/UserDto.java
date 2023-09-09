package ru.practicum.shareit.user.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private final String name;
    private final String email;
    long id;
}
