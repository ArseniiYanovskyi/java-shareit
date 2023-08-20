package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User convertToUser(UserDto userDto) {
        return User.builder()

                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static String getEmail(UserDto userDto) {
        return userDto.getEmail();
    }

    public static String getName(UserDto userDto) {
        return userDto.getName();
    }
}
