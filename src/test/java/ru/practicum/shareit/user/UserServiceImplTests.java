package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("UserService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTests {
    private final EntityManager entityManager;
    private final UserServiceImpl userService;
    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private UserDto thirdUserDto;

    @BeforeEach
    void beforeEach() {
        firstUserDto = UserDto.builder()
                .name("FirstUser")
                .email("FirstUser@somemail.com")
                .build();
        secondUserDto = UserDto.builder()
                .name("SecondUser")
                .email("SecondUser@somemail.com")
                .build();
        thirdUserDto = UserDto.builder()
                .name("ThirdUser")
                .email("ThirdUser@somemail.com")
                .build();
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should create users.")
    void shouldCreateUsers() {
        firstUserDto = userService.addUser(firstUserDto);

        TypedQuery<User> firstUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User firstUser = firstUserQuery.setParameter
                ("id", firstUserDto.getId()).getSingleResult();

        System.out.println("result = " + firstUser + " comparing to expected = " + firstUserDto);
        assertThat(firstUser.getId(), equalTo(firstUserDto.getId()));
        assertThat(firstUser.getName(), equalTo(firstUserDto.getName()));
        assertThat(firstUser.getEmail(), equalTo(firstUserDto.getEmail()));

        secondUserDto = userService.addUser(secondUserDto);

        TypedQuery<User> secondUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User secondUser = secondUserQuery.setParameter
                ("id", secondUserDto.getId()).getSingleResult();

        System.out.println("result = " + secondUser + " comparing to expected = " + secondUserDto);
        assertThat(secondUser.getId(), equalTo(secondUserDto.getId()));
        assertThat(secondUser.getName(), equalTo(secondUserDto.getName()));
        assertThat(secondUser.getEmail(), equalTo(secondUserDto.getEmail()));

        thirdUserDto = userService.addUser(thirdUserDto);

        TypedQuery<User> thirdUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User thirdUser = thirdUserQuery.setParameter
                ("id", thirdUserDto.getId()).getSingleResult();

        System.out.println("result = " + thirdUser + " comparing to expected = " + thirdUserDto);
        assertThat(thirdUser.getId(), equalTo(thirdUserDto.getId()));
        assertThat(thirdUser.getName(), equalTo(thirdUserDto.getName()));
        assertThat(thirdUser.getEmail(), equalTo(thirdUserDto.getEmail()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update users.")
    void shouldUpdateUsers() {
        firstUserDto = userService.addUser(firstUserDto);
        firstUserDto.setName("FirstUserUpdated");
        firstUserDto.setEmail("FirstUserUpdated@somemail.com");
        firstUserDto = userService.updateUser(firstUserDto.getId(), firstUserDto);

        TypedQuery<User> firstUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User firstUser = firstUserQuery.setParameter
                ("id", firstUserDto.getId()).getSingleResult();

        System.out.println("result = " + firstUser + " comparing to expected = " + firstUserDto);
        assertThat(firstUser.getId(), equalTo(firstUserDto.getId()));
        assertThat(firstUser.getName(), equalTo(firstUserDto.getName()));
        assertThat(firstUser.getEmail(), equalTo(firstUserDto.getEmail()));

        secondUserDto = userService.addUser(secondUserDto);
        secondUserDto.setName("SecondUserUpdated");
        secondUserDto.setEmail("SecondUserUpdated@somemail.com");
        secondUserDto = userService.updateUser(secondUserDto.getId(), secondUserDto);

        TypedQuery<User> secondUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User secondUser = secondUserQuery.setParameter
                ("id", secondUserDto.getId()).getSingleResult();

        System.out.println("result = " + secondUser + " comparing to expected = " + secondUserDto);
        assertThat(secondUser.getId(), equalTo(secondUserDto.getId()));
        assertThat(secondUser.getName(), equalTo(secondUserDto.getName()));
        assertThat(secondUser.getEmail(), equalTo(secondUserDto.getEmail()));

        thirdUserDto = userService.addUser(thirdUserDto);
        thirdUserDto.setName("ThirdUserUpdated");
        thirdUserDto.setEmail("ThirdUserUpdated@somemail.com");
        thirdUserDto = userService.updateUser(thirdUserDto.getId(), thirdUserDto);

        TypedQuery<User> thirdUserQuery = entityManager.createQuery
                ("Select i from User i where i.id = :id", User.class);
        User thirdUser = thirdUserQuery.setParameter
                ("id", thirdUserDto.getId()).getSingleResult();

        System.out.println("result = " + thirdUser + " comparing to expected = " + thirdUserDto);
        assertThat(thirdUser.getId(), equalTo(thirdUserDto.getId()));
        assertThat(thirdUser.getName(), equalTo(thirdUserDto.getName()));
        assertThat(thirdUser.getEmail(), equalTo(thirdUserDto.getEmail()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get user DTO by id.")
    void shouldGetUserDtoById() {
        firstUserDto = userService.addUser(firstUserDto);

        UserDto resultingDto = userService.getUserDtoById(firstUserDto.getId());

        System.out.println("result = " + resultingDto + " comparing to expected = " + firstUserDto);
        assertThat(resultingDto.getId(), equalTo(firstUserDto.getId()));
        assertThat(resultingDto.getName(), equalTo(firstUserDto.getName()));
        assertThat(resultingDto.getEmail(), equalTo(firstUserDto.getEmail()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get user by id.")
    void shouldGetUserById() {
        firstUserDto = userService.addUser(firstUserDto);

        User resultingUser = userService.getUserById(firstUserDto.getId());

        System.out.println("result = " + resultingUser + " comparing to expected = " + firstUserDto);
        assertThat(resultingUser.getId(), equalTo(firstUserDto.getId()));
        assertThat(resultingUser.getName(), equalTo(firstUserDto.getName()));
        assertThat(resultingUser.getEmail(), equalTo(firstUserDto.getEmail()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should get all users.")
    void shouldGetAllUsers() {
        firstUserDto = userService.addUser(firstUserDto);

        secondUserDto = userService.addUser(secondUserDto);

        thirdUserDto = userService.addUser(thirdUserDto);

        List<UserDto> expectedList = new ArrayList<>();
        expectedList.add(firstUserDto);
        expectedList.add(secondUserDto);
        expectedList.add(thirdUserDto);

        List<UserDto> resultList = userService.getAllUsers();

        System.out.println("result = " + resultList + " comparing to expected = " + expectedList);
        assertThat(resultList.size(), equalTo(expectedList.size()));

        assertThat(resultList.get(0).getId(), equalTo(expectedList.get(0).getId()));
        assertThat(resultList.get(0).getName(), equalTo(expectedList.get(0).getName()));
        assertThat(resultList.get(0).getEmail(), equalTo(expectedList.get(0).getEmail()));

        assertThat(resultList.get(1).getId(), equalTo(expectedList.get(1).getId()));
        assertThat(resultList.get(1).getName(), equalTo(expectedList.get(1).getName()));
        assertThat(resultList.get(1).getEmail(), equalTo(expectedList.get(1).getEmail()));

        assertThat(resultList.get(2).getId(), equalTo(expectedList.get(2).getId()));
        assertThat(resultList.get(2).getName(), equalTo(expectedList.get(2).getName()));
        assertThat(resultList.get(2).getEmail(), equalTo(expectedList.get(2).getEmail()));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should throw exception if user not present.")
    void shouldThrowExceptionIfUserNotPresent() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserDtoById(50));
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(50));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - should throw exception for empty name.")
    void shouldThrowExceptionIfNameIsEmpty() {
        final UserDto finalFirstUserDto = userService.addUser(firstUserDto);

        secondUserDto.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> userService.addUser(secondUserDto));
        secondUserDto.setName("");
        Assertions.assertThrows(ValidationException.class, () -> userService.addUser(secondUserDto));

        finalFirstUserDto.setName("");
        Assertions.assertThrows(ValidationException.class,
                () -> userService.updateUser(finalFirstUserDto.getId(), finalFirstUserDto));
    }
}
