package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDto {
    long id;
    private String name;
    private String email;
}
