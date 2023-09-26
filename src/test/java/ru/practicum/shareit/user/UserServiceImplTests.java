package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.service.utils.UserServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTests {
    @Mock
    UserRepository userRepository;
    @Mock
    UserServiceUtils utils;
    @InjectMocks
    UserServiceImpl userService;
    UserDto firstUserDto;
    UserDto secondUserDto;
    User firstUser;
    User secondUser;

    @BeforeEach
    void beforeEach() {
        firstUserDto = UserDto.builder()
                .id(1)
                .name("FirstUserName.")
                .email("FirstUserEmail@somemail.com")
                .build();
        secondUserDto = UserDto.builder()
                .id(2)
                .name("SecondUserName.")
                .email("SecondUserEmail@somemail.com")
                .build();

        firstUser = new User(1, "FirstUserName.", "FirstUserEmail@somemail.com");
        secondUser = new User(2, "SecondUserName.", "SecondUserEmail@somemail.com");
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should add user.")
    void shouldAddUser() {
        when(utils.convertToUser(firstUserDto)).thenReturn(firstUser);
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(utils.convertToDto(firstUser)).thenReturn(firstUserDto);

        firstUserDto = userService.addUser(firstUserDto);

        System.out.println("result = " + firstUserDto + " comparing to expected = " + firstUser);
        assertThat(firstUserDto.getId(), equalTo(firstUser.getId()));
        assertThat(firstUserDto.getName(), equalTo(firstUser.getName()));
        assertThat(firstUserDto.getEmail(), equalTo(firstUser.getEmail()));

        verify(utils, times(1)).convertToUser(firstUserDto);
        verify(userRepository, times(1)).save(firstUser);
        verify(utils, times(1)).convertToDto(firstUser);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update users.")
    void shouldUpdateUsers() {
        when(utils.convertToUser(firstUserDto)).thenReturn(firstUser);
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(utils.convertToDto(firstUser)).thenReturn(firstUserDto);

        firstUserDto = userService.addUser(firstUserDto);

        firstUserDto.setName("UpdatedName.");
        firstUserDto.setEmail("UpdatedEmail.");

        firstUser.setName("UpdatedName.");

        when(userRepository.findById(firstUserDto.getId())).thenReturn(Optional.ofNullable(firstUser));
        firstUserDto = userService.updateUser(1, firstUserDto);

        System.out.println("result = " + firstUserDto + " comparing to expected = " + firstUser);
        assertThat(firstUserDto.getId(), equalTo(firstUser.getId()));
        assertThat(firstUserDto.getName(), equalTo(firstUser.getName()));
        assertThat(firstUserDto.getEmail(), equalTo(firstUser.getEmail()));

        verify(userRepository, times(1)).findById(firstUserDto.getId());
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get user DTO by id.")
    void shouldGetUserDtoById() {
        when(utils.convertToUser(firstUserDto)).thenReturn(firstUser);
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(utils.convertToDto(firstUser)).thenReturn(firstUserDto);

        firstUserDto = userService.addUser(firstUserDto);

        when(userRepository.findById(firstUserDto.getId())).thenReturn(Optional.ofNullable(firstUser));

        UserDto result = userService.getUserDtoById(firstUserDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstUser);
        assertThat(result.getId(), equalTo(firstUserDto.getId()));
        assertThat(result.getName(), equalTo(firstUserDto.getName()));
        assertThat(result.getEmail(), equalTo(firstUserDto.getEmail()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get user by id.")
    void shouldGetUserById() {
        when(userRepository.findById(firstUser.getId())).thenReturn(Optional.ofNullable(firstUser));

        User result = userService.getUserById(firstUser.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstUser);
        assertThat(result.getId(), equalTo(firstUser.getId()));
        assertThat(result.getName(), equalTo(firstUser.getName()));
        assertThat(result.getEmail(), equalTo(firstUser.getEmail()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should throw exception if user not present.")
    void shouldThrowExceptionIfUserNotPresent() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserDtoById(50));
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(50));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should throw exception if user not present.")
    void shouldGetAllUsers() {
        List<User> expecting = new ArrayList<>();
        expecting.add(firstUser);
        expecting.add(secondUser);
        when(userRepository.findAll()).thenReturn(expecting);
        when(utils.convertToDto(firstUser)).thenReturn(firstUserDto);
        when(utils.convertToDto(secondUser)).thenReturn(secondUserDto);

        List<UserDto> result = userService.getAllUsers();

        System.out.println("result = " + result + " comparing to expected = " + firstUserDto + secondUserDto);
        assertThat(result.get(0).getId(), equalTo(firstUserDto.getId()));
        assertThat(result.get(0).getName(), equalTo(firstUserDto.getName()));
        assertThat(result.get(0).getEmail(), equalTo(firstUserDto.getEmail()));

        assertThat(result.get(1).getId(), equalTo(secondUserDto.getId()));
        assertThat(result.get(1).getName(), equalTo(secondUserDto.getName()));
        assertThat(result.get(1).getEmail(), equalTo(secondUserDto.getEmail()));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - should throw exception if user not present.")
    void shouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(firstUser));
        userService.deleteUser(1);
    }
}
