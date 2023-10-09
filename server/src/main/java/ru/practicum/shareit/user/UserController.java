package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Received request to add new user.");

        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") long userId,
                              @RequestBody UserDto userDto) {
        log.info("Received request to update existed information about user with id {}.", userId);

        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable(value = "userId") long userId) {
        log.info("Received request to get existed user with id {}.", userId);

        return userService.getUserDtoById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Received request to get all users.");

        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(value = "userId") long userId) {
        log.info("Received request to delete user with id {}.", userId);

        userService.deleteUser(userId);
    }
}
