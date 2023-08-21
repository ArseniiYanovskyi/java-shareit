package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(long userId);

    List<User> getAllUsers();

    boolean isEmailAvailable(String email);

    void deleteUser(long userId);
}
