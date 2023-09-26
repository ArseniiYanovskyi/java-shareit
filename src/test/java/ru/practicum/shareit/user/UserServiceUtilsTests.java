package ru.practicum.shareit.user;


import org.junit.jupiter.api.*;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.utils.UserServiceUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceUtilsTests {
    private final UserServiceUtils utils;
    private UserDto firstUserDto;
    private User firstUser;

    public UserServiceUtilsTests() {
        this.utils = new UserServiceUtils();
    }

    @BeforeEach
    void beforeEach() {
        firstUserDto = UserDto.builder()
                .id(1)
                .name("FirstUserName.")
                .email("FirstUserEmail@somemail.com")
                .build();
        firstUser = new User(1, "FirstUserName.", "FirstUserEmail@somemail.com");
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - check is userDto valid.")
    void shouldCheckUserDtoValidation() {
        firstUserDto.setName(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkIsUserValid(firstUserDto));
        firstUserDto.setName("");
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkIsUserValid(firstUserDto));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - check name for updating.")
    void shouldCheckUserNameForUpdating() {
        firstUserDto.setName("");
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkNameForUpdating(firstUserDto));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should convert to user.")
    void shouldConvertToUserFromDto() {
        User result = utils.convertToUser(firstUserDto);
        assertThat(result.getId(), equalTo(firstUser.getId()));
        assertThat(result.getName(), equalTo(firstUser.getName()));
        assertThat(result.getEmail(), equalTo(firstUser.getEmail()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should convert to DTO.")
    void shouldConvertToDtoFromUser() {
        UserDto result = utils.convertToDto(firstUser);
        assertThat(result.getId(), equalTo(firstUserDto.getId()));
        assertThat(result.getName(), equalTo(firstUserDto.getName()));
        assertThat(result.getEmail(), equalTo(firstUserDto.getEmail()));
    }
}
