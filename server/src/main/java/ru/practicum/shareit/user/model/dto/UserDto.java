package ru.practicum.shareit.user.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    long id;
    private String name;
    private String email;
}
