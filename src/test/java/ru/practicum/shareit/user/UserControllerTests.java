package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTests {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should add new users.")
    void shouldAddNewUsers() throws Exception {
        UserDto firstUser = createFirstUserDto();
        firstUser.setId(1);
        when(userService.addUser(firstUser))
                .thenReturn(firstUser);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(firstUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstUser.getName())))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail())));

        UserDto secondUser = createSecondUserDto();
        secondUser.setId(2);
        when(userService.addUser(secondUser))
                .thenReturn(secondUser);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(secondUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is(secondUser.getName())))
                .andExpect(jsonPath("$.email", is(secondUser.getEmail())));

        UserDto thirdUser = createThirdUserDto();
        thirdUser.setId(3);
        when(userService.addUser(thirdUser))
                .thenReturn(thirdUser);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(thirdUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is(thirdUser.getName())))
                .andExpect(jsonPath("$.email", is(thirdUser.getEmail())));

        verify(userService, times(1)).addUser(firstUser);
        verify(userService, times(1)).addUser(secondUser);
        verify(userService, times(1)).addUser(thirdUser);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update users.")
    void shouldUpdateUsers() throws Exception {
        UserDto firstUser = createFirstUserDto();
        firstUser.setId(1);
        when(userService.updateUser(firstUser.getId(), firstUser))
                .thenReturn(firstUser);

        mockMvc.perform(patch("/users/{userId}", 1)
                        .content(mapper.writeValueAsString(firstUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstUser.getName())))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail())));

        verify(userService, times(1)).updateUser(firstUser.getId(), firstUser);
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should return user by id.")
    void shouldReturnUserById() throws Exception {
        UserDto firstUser = createFirstUserDto();
        firstUser.setId(1);
        when(userService.getUserDtoById(firstUser.getId()))
                .thenReturn(firstUser);

        mockMvc.perform(get("/users/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstUser.getName())))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail())));

        verify(userService, times(1)).getUserDtoById(firstUser.getId());
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should return all users.")
    void shouldGetAllUsers() throws Exception {
        UserDto firstUser = createFirstUserDto();
        firstUser.setId(1);
        UserDto secondUser = createSecondUserDto();
        secondUser.setId(2);
        UserDto thirdUser = createThirdUserDto();
        thirdUser.setId(3);
        List<UserDto> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);
        users.add(thirdUser);

        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is(users.get(1).getName())))
                .andExpect(jsonPath("$[1].email", is(users.get(1).getEmail())))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is(users.get(2).getName())))
                .andExpect(jsonPath("$[2].email", is(users.get(2).getEmail())));

        verify(userService, times(1)).getAllUsers();
    }

    private UserDto createFirstUserDto() {
        return UserDto.builder()
                .name("FirstUser")
                .email("FirstUser@somemail.com")
                .build();
    }

    private UserDto createSecondUserDto() {
        return UserDto.builder()
                .name("SecondUser")
                .email("SecondUser@somemail.com")
                .build();
    }

    private UserDto createThirdUserDto() {
        return UserDto.builder()
                .name("ThirdUser")
                .email("ThirdUser@somemail.com")
                .build();
    }
}
