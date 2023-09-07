package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.utils.UserServiceUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserServiceUtils utils;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        utils.checkIsUserValid(userDto);

        User user = utils.convertToUser(userDto);

        log.debug("Sending to DAO information to add new user.");

        return utils.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = getUserById(userId);
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            utils.checkNameForUpdating(userDto);
            user.setName(userDto.getName());
        }
        log.debug("Sending to DAO updated user {} information.", userId);
        return utils.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        log.debug("Sending to DAO request to get all users.");

        return userRepository.findAll().stream()
                .map(utils::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto getUserDtoById(long userId) {
        log.debug("Sending to DAO request to get user with id {}.", userId);

        return utils.convertToDto(getUserById(userId));

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
}
