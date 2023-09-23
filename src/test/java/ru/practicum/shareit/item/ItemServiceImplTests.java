package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
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
@DisplayName("ItemService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemServiceImplTests {
    private final EntityManager entityManager;
    @Autowired
    UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    private ItemDto firstItemDto;
    private ItemDto secondItemDto;
    private ItemDto thirdItemDto;

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
        firstItemDto = ItemDto.builder()
                    .name("FirstItemName")
                    .description("FirstItemDescription")
                    .available(true)
                    .build();
        secondItemDto = ItemDto.builder()
                    .name("SecondItemName")
                    .description("SecondItemDescription")
                    .available(true)
                    .build();
        thirdItemDto = ItemDto.builder()
                    .name("ThirdItemName")
                    .description("ThirdItemDescription")
                    .available(true)
                    .requestId(1)
                    .build();
    }

    @AfterEach
    void afterEach() {
    }


    @Test
    @Order(value = 1)
    @DisplayName("1 - should add items.")
    void shouldAddItems() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        TypedQuery<Item> firstItemQuery = entityManager.createQuery
                ("Select i from Item i where i.id = :id", Item.class);
        Item firstItem = firstItemQuery.setParameter
                ("id", firstItemDto.getId()).getSingleResult();

        System.out.println("result = " + firstItem + " comparing to expected = " + firstItemDto);
        assertThat(firstItem.getId(), equalTo(firstItemDto.getId()));
        assertThat(firstItem.getName(), equalTo(firstItemDto.getName()));
        assertThat(firstItem.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(firstItem.getIsAvailable(), equalTo(firstItemDto.getAvailable()));

        secondItemDto = itemService.addItem(2, secondItemDto);

        TypedQuery<Item> secondItemQuery = entityManager.createQuery
                ("Select i from Item i where i.id = :id", Item.class);
        Item secondItem = secondItemQuery.setParameter
                ("id", secondItemDto.getId()).getSingleResult();

        System.out.println("result = " + secondItem + " comparing to expected = " + secondItemDto);
        assertThat(secondItem.getId(), equalTo(secondItemDto.getId()));
        assertThat(secondItem.getName(), equalTo(secondItemDto.getName()));
        assertThat(secondItem.getDescription(), equalTo(secondItemDto.getDescription()));
        assertThat(secondItem.getIsAvailable(), equalTo(secondItemDto.getAvailable()));

        thirdItemDto = itemService.addItem(3, thirdItemDto);

        TypedQuery<Item> thirdItemQuery = entityManager.createQuery
                ("Select i from Item i where i.id = :id", Item.class);
        Item thirdItem = thirdItemQuery.setParameter
                ("id", thirdItemDto.getId()).getSingleResult();

        System.out.println("result = " + thirdItem + " comparing to expected = " + thirdItemDto);
        assertThat(thirdItem.getId(), equalTo(thirdItemDto.getId()));
        assertThat(thirdItem.getName(), equalTo(thirdItemDto.getName()));
        assertThat(thirdItem.getDescription(), equalTo(thirdItemDto.getDescription()));
        assertThat(thirdItem.getIsAvailable(), equalTo(thirdItemDto.getAvailable()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update item.")
    void shouldUpdateItem() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        firstItemDto.setName("newFirstItem");
        firstItemDto.setDescription("newFirstItemDescription");
        firstItemDto.setAvailable(false);

        firstItemDto = itemService.updateItem(1, firstItemDto);

        TypedQuery<Item> firstItemQuery = entityManager.createQuery
                ("Select i from Item i where i.id = :id", Item.class);
        Item firstItem = firstItemQuery.setParameter
                ("id", firstItemDto.getId()).getSingleResult();

        System.out.println("result = " + firstItem + " comparing to expected = " + firstItemDto);
        assertThat(firstItem.getId(), equalTo(firstItemDto.getId()));
        assertThat(firstItem.getName(), equalTo(firstItemDto.getName()));
        assertThat(firstItem.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(firstItem.getIsAvailable(), equalTo(firstItemDto.getAvailable()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get item DTO by id.")
    void shouldGetItemDtoById() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        ItemDto result = itemService.getItemDtoById(firstItemDto.getId(), 1);

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItemDto.getAvailable()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get item by id.")
    void shouldGetItemById() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        Item result = itemService.getItemById(firstItemDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getIsAvailable(), equalTo(firstItemDto.getAvailable()));
    }
    @Test
    @Order(value = 5)
    @DisplayName("5 - should get all user's items.")
    void shouldGetAllUserItems() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        secondItemDto = itemService.addItem(1, secondItemDto);

        thirdItemDto = itemService.addItem(2, thirdItemDto);

        List<ItemDto> expectedList = new ArrayList<>();
        expectedList.add(firstItemDto);
        expectedList.add(secondItemDto);

        List<ItemDto> resultList = itemService.getItemsByUserId(1);

        System.out.println("result = " + resultList + " comparing to expected = " + expectedList);
        assertThat(resultList.size(), equalTo(expectedList.size()));
        assertThat(resultList.get(0).getName(), equalTo(expectedList.get(0).getName()));
        assertThat(resultList.get(0).getDescription(), equalTo(expectedList.get(0).getDescription()));
        assertThat(resultList.get(0).getAvailable(), equalTo(expectedList.get(0).getAvailable()));
        assertThat(resultList.get(1).getName(), equalTo(expectedList.get(1).getName()));
        assertThat(resultList.get(1).getDescription(), equalTo(expectedList.get(1).getDescription()));
        assertThat(resultList.get(1).getAvailable(), equalTo(expectedList.get(1).getAvailable()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should search in description.")
    void shouldSearchInDescription() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        secondItemDto = itemService.addItem(2, secondItemDto);

        thirdItemDto = itemService.addItem(3, thirdItemDto);

        List<ItemDto> result = itemService.searchInDescription("thi");

        System.out.println("result = " + result + " comparing to expected = " + thirdItemDto);
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getId(), equalTo(thirdItemDto.getId()));
        assertThat(result.get(0).getName(), equalTo(thirdItemDto.getName()));
        assertThat(result.get(0).getDescription(), equalTo(thirdItemDto.getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(thirdItemDto.getAvailable()));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should get items by request.")
    void shouldGetItemsByRequest() {
        firstItemDto = itemService.addItem(1, firstItemDto);

        secondItemDto = itemService.addItem(2, secondItemDto);

        thirdItemDto = itemService.addItem(3, thirdItemDto);

        List<ItemDto> result = itemService.getItemsForRequest(1);
        System.out.println("result = " + result + " comparing to expected = " + thirdItemDto);
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getId(), equalTo(thirdItemDto.getId()));
        assertThat(result.get(0).getName(), equalTo(thirdItemDto.getName()));
        assertThat(result.get(0).getDescription(), equalTo(thirdItemDto.getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(thirdItemDto.getAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(thirdItemDto.getRequestId()));
    }
}
