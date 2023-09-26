package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DisplayName("ItemRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    private Item firstItem;
    private Item secondItem;
    private Item thirdItem;
    private Item fourthItem;
    private Item fifthItem;
    private User firstUser;
    private User secondUser;

    @BeforeEach
    void beforeEach() {
        firstUser = userRepository.save(new User(1L, "FirstUser", "FirstUser@somemail.com"));
        secondUser = userRepository.save(new User(2L, "SecondUser", "SecondUser@somemail.com"));
        firstItem = itemRepository.save(new Item(1L, "FirstItemName", "FirstItemDescription",
                true, firstUser, 0));
        secondItem = itemRepository.save(new Item(2L, "SecondItemName", "SecondItemDescription",
                true, firstUser, 0));
        thirdItem = itemRepository.save(new Item(3L, "ThirdItemName", "ThirdItemDescription",
                true, firstUser, 1));
        fourthItem = itemRepository.save(new Item(4L, "FourthItemName", "FourthItemDescription",
                false, secondUser, 1));
        fifthItem = itemRepository.save(new Item(5L, "FifthItemName", "FifthItemDescription",
                true, secondUser, 1));
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - find by owner id.")
    void shouldFindByOwnerId() {
        List<Item> expectingResult = new ArrayList<>();
        expectingResult.add(fourthItem);
        expectingResult.add(fifthItem);

        List<Item> actualResult = itemRepository.findAllByOwner_IdOrderByIdAsc(2);

        assertThat(expectingResult.size(), equalTo(actualResult.size()));

        assertThat(expectingResult.get(0).getName(), equalTo(actualResult.get(0).getName()));
        assertThat(expectingResult.get(0).getDescription(), equalTo(actualResult.get(0).getDescription()));
        assertThat(expectingResult.get(0).getIsAvailable(), equalTo(actualResult.get(0).getIsAvailable()));
        assertThat(expectingResult.get(0).getOwner().getId(), equalTo(actualResult.get(0).getOwner().getId()));
        assertThat(expectingResult.get(0).getRequest(), equalTo(actualResult.get(0).getRequest()));

        assertThat(expectingResult.get(1).getName(), equalTo(actualResult.get(1).getName()));
        assertThat(expectingResult.get(1).getDescription(), equalTo(actualResult.get(1).getDescription()));
        assertThat(expectingResult.get(1).getIsAvailable(), equalTo(actualResult.get(1).getIsAvailable()));
        assertThat(expectingResult.get(1).getOwner().getId(), equalTo(actualResult.get(1).getOwner().getId()));
        assertThat(expectingResult.get(1).getRequest(), equalTo(actualResult.get(1).getRequest()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should find by description.")
    void shouldFindByDescription() {
        List<Item> expectingResult = new ArrayList<>();
        expectingResult.add(fifthItem);

        List<Item> actualResult = itemRepository.findAllByDescriptionContainsIgnoreCase("fif");

        assertThat(expectingResult.size(), equalTo(actualResult.size()));

        assertThat(expectingResult.get(0).getName(), equalTo(actualResult.get(0).getName()));
        assertThat(expectingResult.get(0).getDescription(), equalTo(actualResult.get(0).getDescription()));
        assertThat(expectingResult.get(0).getIsAvailable(), equalTo(actualResult.get(0).getIsAvailable()));
        assertThat(expectingResult.get(0).getOwner().getId(), equalTo(actualResult.get(0).getOwner().getId()));
        assertThat(expectingResult.get(0).getRequest(), equalTo(actualResult.get(0).getRequest()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should find by request.")
    void shouldFindByRequest() {
        List<Item> expectingResult = new ArrayList<>();
        expectingResult.add(thirdItem);
        expectingResult.add(fourthItem);
        expectingResult.add(fifthItem);

        List<Item> actualResult = itemRepository.findAllByRequest(1);

        assertThat(expectingResult.size(), equalTo(actualResult.size()));

        assertThat(expectingResult.get(0).getName(), equalTo(actualResult.get(0).getName()));
        assertThat(expectingResult.get(0).getDescription(), equalTo(actualResult.get(0).getDescription()));
        assertThat(expectingResult.get(0).getIsAvailable(), equalTo(actualResult.get(0).getIsAvailable()));
        assertThat(expectingResult.get(0).getOwner().getId(), equalTo(actualResult.get(0).getOwner().getId()));
        assertThat(expectingResult.get(0).getRequest(), equalTo(actualResult.get(0).getRequest()));

        assertThat(expectingResult.get(1).getName(), equalTo(actualResult.get(1).getName()));
        assertThat(expectingResult.get(1).getDescription(), equalTo(actualResult.get(1).getDescription()));
        assertThat(expectingResult.get(1).getIsAvailable(), equalTo(actualResult.get(1).getIsAvailable()));
        assertThat(expectingResult.get(1).getOwner().getId(), equalTo(actualResult.get(1).getOwner().getId()));
        assertThat(expectingResult.get(1).getRequest(), equalTo(actualResult.get(1).getRequest()));

        assertThat(expectingResult.get(2).getName(), equalTo(actualResult.get(2).getName()));
        assertThat(expectingResult.get(2).getDescription(), equalTo(actualResult.get(2).getDescription()));
        assertThat(expectingResult.get(2).getIsAvailable(), equalTo(actualResult.get(2).getIsAvailable()));
        assertThat(expectingResult.get(2).getOwner().getId(), equalTo(actualResult.get(2).getOwner().getId()));
        assertThat(expectingResult.get(2).getRequest(), equalTo(actualResult.get(2).getRequest()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - find by owner id pagination.")
    void shouldFindByOwnerIdPagination() {
        List<Item> expectingResult = new ArrayList<>();
        expectingResult.add(fourthItem);
        expectingResult.add(fifthItem);
        final PageImpl<Item> expectingResultPage = new PageImpl<>(expectingResult);

        Page<Item> actualResults = itemRepository.findAllByOwner_IdOrderByIdAsc
                (secondUser.getId(), PageRequest.ofSize(10));

        assertThat(expectingResultPage.getTotalElements(), equalTo(actualResults.getTotalElements()));

        Item expectingFirstItem = actualResults.stream()
                .filter(item -> item.getId() == expectingResult.get(0).getId())
                .findFirst()
                .get();
        Item exceptingSecondItem = actualResults.stream()
                .filter(item -> item.getId() == expectingResult.get(1).getId())
                .findFirst()
                .get();

        assertThat(expectingResult.get(0).getName(), equalTo(expectingFirstItem.getName()));
        assertThat(expectingResult.get(0).getDescription(), equalTo(expectingFirstItem.getDescription()));
        assertThat(expectingResult.get(0).getIsAvailable(), equalTo(expectingFirstItem.getIsAvailable()));
        assertThat(expectingResult.get(0).getOwner().getId(), equalTo(expectingFirstItem.getOwner().getId()));
        assertThat(expectingResult.get(0).getRequest(), equalTo(expectingFirstItem.getRequest()));

        assertThat(expectingResult.get(1).getName(), equalTo(exceptingSecondItem.getName()));
        assertThat(expectingResult.get(1).getDescription(), equalTo(exceptingSecondItem.getDescription()));
        assertThat(expectingResult.get(1).getIsAvailable(), equalTo(exceptingSecondItem.getIsAvailable()));
        assertThat(expectingResult.get(1).getOwner().getId(), equalTo(exceptingSecondItem.getOwner().getId()));
        assertThat(expectingResult.get(1).getRequest(), equalTo(exceptingSecondItem.getRequest()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should find by description pagination.")
    void shouldFindByDescriptionPagination() {
        List<Item> expectingResult = new ArrayList<>();
        expectingResult.add(fifthItem);
        final PageImpl<Item> expectingResultPage = new PageImpl<>(expectingResult);

        Page<Item> actualResult = itemRepository.findAllByDescriptionContainsIgnoreCase
                ("fift", PageRequest.ofSize(10));

        assertThat(expectingResultPage.getTotalElements(), equalTo(actualResult.getTotalElements()));

        Item expectingFirstItem = actualResult.stream()
                .filter(item -> item.getId() == expectingResult.get(0).getId())
                .findFirst()
                .get();

        assertThat(expectingResult.get(0).getName(), equalTo(expectingFirstItem.getName()));
        assertThat(expectingResult.get(0).getDescription(), equalTo(expectingFirstItem.getDescription()));
        assertThat(expectingResult.get(0).getIsAvailable(), equalTo(expectingFirstItem.getIsAvailable()));
        assertThat(expectingResult.get(0).getOwner().getId(), equalTo(expectingFirstItem.getOwner().getId()));
        assertThat(expectingResult.get(0).getRequest(), equalTo(expectingFirstItem.getRequest()));
    }
}
