package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.AlreadyUsedException;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userRepository;
    private final String CORRECT_EMAIL_REGEXP = "\\S.*@\\S.*\\..*";
    private final Logger log = LoggerFactory.getLogger("UserService");

    @Override
    public UserDto addUser(UserDto userDto) {
        checkNewUserDtoValidation(userDto);
        checkIsEmailAvailable(getEmail(userDto));

        User user = convertToUser(userDto);

        log.debug("Sending to DAO information to add new user.");

        return convertToDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        checkIsUserPresent(userId);
        if (checkNameForUpdating(userDto)) {
            log.debug("Sending to DAO name to update user {} information.", userId);

            userRepository.updateUserName(userId, getName(userDto));
        }
        if (checkEmailForUpdating(userDto)) {
            if (!getUserById(userId).getEmail().equals(getEmail(userDto))) {
                checkIsEmailAvailable(getEmail(userDto));
            }
            log.debug("Sending to DAO email to update user {} information.", userId);

            userRepository.updateUserEmail(userId, getEmail(userDto));
        }
        return getUserById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Sending to DAO request to get all users.");

        return userRepository.getAllUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        log.debug("Sending to DAO request to get user with id {}.", userId);

        return convertToDto(userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " does not present in repository.")));

    }

    @Override
    public void checkIsUserPresent(long userId) {
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " does not present in repository."));
    }

    @Override
    public void deleteUser(long userId) {
        checkIsUserPresent(userId);

        log.debug("Sending to DAO request to delete user with id {}.", userId);

        userRepository.deleteUser(userId);
    }

    private void checkNewUserDtoValidation(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Name is blank");
        }
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email information empty.");
        }
        if (!userDto.getEmail().matches(CORRECT_EMAIL_REGEXP)) {
            throw new ValidationException("Incorrect email");
        }
    }

    private boolean checkNameForUpdating(UserDto userDto) {
        if (userDto.getName() != null) {
            if (userDto.getName().isBlank()) {
                throw new ValidationException("Name is blank");
            }
            return true;
        }
        return false;
    }

    private boolean checkEmailForUpdating(UserDto userDto) {
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().matches(CORRECT_EMAIL_REGEXP)) {
                throw new ValidationException("Incorrect email");
            }
            return true;
        }
        return false;
    }

    private void checkIsEmailAvailable(String email) {
        if (!userRepository.isEmailAvailable(email)) {
            throw new AlreadyUsedException("Email already used.");
        }
    }

    private User convertToUser(UserDto userDto) {
        return UserMapper.convertToUser(userDto);
    }

    private UserDto convertToDto(User user) {
        return UserMapper.convertToDto(user);
    }

    private String getName(UserDto userDto) {
        return UserMapper.getName(userDto);
    }

    private String getEmail(UserDto userDto) {
        return UserMapper.getEmail(userDto);
    }
}
