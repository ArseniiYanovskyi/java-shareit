package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.AlreadyUsedException;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final String CORRECT_EMAIL_REGEXP = "\\S.*@\\S.*\\..*";

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        checkIsUserValid(userDto);
        //checkIsEmailAvailable(userDto.getEmail());

        User user = convertToUser(userDto);

        log.debug("Sending to DAO information to add new user.");

        return convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = getUserById(userId);
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            checkIsEmailAvailable(userDto.getEmail());
            checkIsEmailValid(userDto.getEmail());
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            checkNameForUpdating(userDto);
            user.setName(userDto.getName());
        }
        log.debug("Sending to DAO updated user {} information.", userId);
        return convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        log.debug("Sending to DAO request to get all users.");

        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto getUserDtoById(long userId) {
        log.debug("Sending to DAO request to get user with id {}.", userId);

        return convertToDto(getUserById(userId));

    }
    @Override
    @Transactional
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " does not present in repository."));
    }

    @Override
    @Transactional
    public void checkIsUserPresent(long userId) {
        getUserById(userId);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        checkIsUserPresent(userId);

        log.debug("Sending to DAO request to delete user with id {}.", userId);

        userRepository.deleteById(userId);
    }

    private void checkIsUserValid(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Name is blank");
        }
        /*if (userDto.getEmail() == null) {
            throw new ValidationException("Email information empty.");
        }
        if (!userDto.getEmail().matches(CORRECT_EMAIL_REGEXP)) {
            throw new ValidationException("Incorrect email");
        }*/
    }

    private void checkNameForUpdating(UserDto userDto) {
        if (userDto.getName().isBlank()) {
            throw new ValidationException("Name is blank");
        }
    }

    private void checkIsEmailValid(String email) {
        if (!email.matches(CORRECT_EMAIL_REGEXP)) {
            throw new ValidationException("Incorrect email");
        }
    }

    private void checkIsEmailAvailable(String email) {
        if (!userRepository.findByEmailContainingIgnoreCase(email).isEmpty()) {
            throw new AlreadyUsedException("Email already used.");
        }
    }


    private User convertToUser(UserDto userDto) {
        return UserMapper.convertToUser(userDto);
    }

    private UserDto convertToDto(User user) {
        return UserMapper.convertToDto(user);
    }
}
