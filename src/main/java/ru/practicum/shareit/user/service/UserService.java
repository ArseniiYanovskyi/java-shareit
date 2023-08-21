package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto getUserDtoById(long userId);

    List<UserDto> getAllUsers();

    void checkIsUserPresent(long userId);

    void deleteUser(long userId);
}
