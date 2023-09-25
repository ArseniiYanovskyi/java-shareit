package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("ItemRequestService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRequestServiceTests {
    private final ItemRequestService itemRequestService;
    private final EntityManager entityManager;
    @Autowired
    UserServiceImpl userService;
    private ItemRequestDto itemRequestDto;

    @BeforeAll
    void beforeAll() {
        userService.addUser(UserMapper.convertToDto
                (new User(1, "FirstUserName", "FirstUser@somemail.com")));
        userService.addUser(UserMapper.convertToDto
                (new User(2, "SecondUserName", "SecondUser@somemail.com")));
        userService.addUser(UserMapper.convertToDto
                (new User(3, "ThirdUserName", "ThirdUser@somemail.com")));
    }

    @BeforeEach
    void beforeEach() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("test description.")
                .created(LocalDateTime.of(2023, 9, 22, 14, 32, 16))
                .items(Collections.singletonList(
                        ItemDto.builder()
                                .id(1)
                                .name("FirstItemName")
                                .description("FirstItemDescription")
                                .available(true)
                                .build()
                ))
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - add new request.")
    void shouldAddNewRequest() {
        long userId = 1;
        itemRequestDto = itemRequestService.addNewRequest(userId, itemRequestDto);

        TypedQuery<ItemRequest> itemRequestTypedQuery = entityManager.createQuery
                ("Select i from ItemRequest i where i.id = :id", ItemRequest.class);
        ItemRequest itemRequest = itemRequestTypedQuery.setParameter
                ("id", itemRequestDto.getId()).getSingleResult();

        System.out.println("result = " + itemRequest + " comparing to expected = " + itemRequestDto);
        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getPublisher(), equalTo(userId));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - get user requests.")
    void shouldGetUserRequests() {
        long userId = 1;
        itemRequestDto = itemRequestService.addNewRequest(userId, itemRequestDto);

        List<ItemRequestDto> itemRequest = itemRequestService.getUserRequests(userId);

        assertThat(itemRequest.size(), equalTo(1));

        System.out.println("result = " + itemRequest + " comparing to expected = " + itemRequestDto);
        assertThat(itemRequest.get(0).getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.get(0).getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - get other users requests.")
    void shouldGetOtherUsersRequests() {
        long userId = 5;
        itemRequestDto = itemRequestService.addNewRequest(1L, itemRequestDto);

        TypedQuery<ItemRequest> itemRequestTypedQuery = entityManager.createQuery
                ("Select i from ItemRequest i where i.publisher != :publisher", ItemRequest.class);
        List<ItemRequestDto> itemRequest = itemRequestService.getOtherUsersRequests(userId);

        System.out.println("result = " + itemRequest + " comparing to expected = " + itemRequestDto);
        assertThat(itemRequest.get(0).getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.get(0).getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - get other users requests pagination.")
    void shouldGetOtherUsersRequestsPagination() {
        long userId = 5;
        itemRequestDto = itemRequestService.addNewRequest(1L, itemRequestDto);

        List<ItemRequestDto> itemRequest = itemRequestService.getOtherUsersRequestsPagination(userId, 1, 10);

        System.out.println("result = " + itemRequest + " comparing to expected = " + itemRequestDto);
        assertThat(itemRequest.get(0).getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.get(0).getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - get request by id.")
    void shouldGetRequestById() {
        long userId = 1;
        itemRequestDto = itemRequestService.addNewRequest(userId, itemRequestDto);

        ItemRequestDto itemRequest = itemRequestService.getRequest(userId, itemRequestDto.getId());

        System.out.println("result = " + itemRequest + " comparing to expected = " + itemRequestDto);
        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}
