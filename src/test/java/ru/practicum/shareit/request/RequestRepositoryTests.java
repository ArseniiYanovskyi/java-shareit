package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DisplayName("RequestRepository")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    RequestRepository requestRepository;
    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private User fourthUser;
    private User fifthUser;
    private User sixthUser;
    private Item firstItem;
    private Item secondItem;
    private Item thirdItem;
    private Item fourthItem;
    private Item fifthItem;
    private ItemRequest firstItemRequest;
    private ItemRequest secondItemRequest;

    @BeforeAll
    void beforeAll() {
        firstUser = userRepository.save(new User(1L, "FirstUser", "FirstUser@somemail.com"));
        secondUser = userRepository.save(new User(2L, "SecondUser", "SecondUser@somemail.com"));
        thirdUser = userRepository.save(new User(3L, "ThirdUser", "ThirdUser@somemail.com"));
        fourthUser = userRepository.save(new User(4L, "FourthUser", "FourthUser@somemail.com"));
        fifthUser = userRepository.save(new User(5L, "FifthUser", "FifthUser@somemail.com"));
        sixthUser = userRepository.save(new User(6L, "SixthUser", "SixthUser@somemail.com"));

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

    @BeforeEach
    void beforeEach() {
        firstItemRequest = new ItemRequest();
        firstItemRequest.setId(1);
        firstItemRequest.setPublisher(1);
        firstItemRequest.setDescription("Looking for test first.");
        firstItemRequest.setCreationDate(LocalDateTime.of(2024, 2, 3, 19, 16, 40));

        secondItemRequest = new ItemRequest();
        secondItemRequest.setId(2);
        secondItemRequest.setPublisher(2);
        secondItemRequest.setDescription("Looking for test second.");
        secondItemRequest.setCreationDate(LocalDateTime.of(2024, 2, 13, 10, 30, 20));

        firstItemRequest = requestRepository.save(firstItemRequest);
        secondItemRequest = requestRepository.save(secondItemRequest);
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - findAllByPublisher.")
    void findFirst() {
        List<ItemRequest> expected = Collections.singletonList(firstItemRequest);
        List<ItemRequest> result = requestRepository.findAllByPublisher(1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getPublisher(), equalTo(result.get(0).getPublisher()));
        assertThat(expected.get(0).getDescription(), equalTo(result.get(0).getDescription()));
        assertThat(expected.get(0).getCreationDate(), equalTo(result.get(0).getCreationDate()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - findAllByPublisherIsNotOrderByCreationDateDesc.")
    void findSecond() {
        List<ItemRequest> expected = Collections.singletonList(firstItemRequest);
        List<ItemRequest> result = requestRepository.findAllByPublisherIsNotOrderByCreationDateDesc(2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getPublisher(), equalTo(result.get(0).getPublisher()));
        assertThat(expected.get(0).getDescription(), equalTo(result.get(0).getDescription()));
        assertThat(expected.get(0).getCreationDate(), equalTo(result.get(0).getCreationDate()));
    }
}
