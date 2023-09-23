package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DisplayName("BookingRepository")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingRepository bookingRepository;

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
    private Booking firstBooking;
    private Booking secondBooking;
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
        firstBooking = new Booking();
        firstBooking.setId(1);
        firstBooking.setBooker(firstUser);
        firstBooking.setItem(fourthItem);
        firstBooking.setStart(LocalDateTime.of(2024, 1, 2, 18, 15, 30));
        firstBooking.setEnd(LocalDateTime.of(2024, 1, 10, 11, 50, 30));
        firstBooking.setStatus(Status.WAITING);

        secondBooking = new Booking();
        secondBooking.setId(2);
        secondBooking.setBooker(secondUser);
        secondBooking.setItem(firstItem);
        secondBooking.setStart(LocalDateTime.of(2024, 2, 3, 19, 16, 40));
        secondBooking.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        secondBooking.setStatus(Status.WAITING);

        firstBooking = bookingRepository.save(firstBooking);
        secondBooking = bookingRepository.save(secondBooking);
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - findAllByBooker_IdOrderByStartDesc.")
    void shouldFindFirst() {
        List<Booking> expected = Collections.singletonList(firstBooking);
        List<Booking> result = bookingRepository.findAllByBooker_IdOrderByStartDesc(1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - findAllByStartBeforeAndEndIsAfterAndBooker_IdIsOrderByStartDesc.")
    void shouldFindSecond() {
        List<Booking> expected = Collections.singletonList(firstBooking);
        List<Booking> result =
                bookingRepository.findAllByStartBeforeAndEndIsAfterAndBooker_IdIsOrderByStartDesc
                        (LocalDateTime.of(2024, 1, 4, 18, 15, 30),
                         LocalDateTime.of(2024, 1, 7, 11, 50, 30),
                                1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }
    @Test
    @Order(value = 3)
    @DisplayName("3 - findAllByEndBeforeAndBooker_IdIsOrderByStartDesc.")
    void shouldFindThird() {
        List<Booking> expected = Collections.singletonList(firstBooking);
        List<Booking> result =
                bookingRepository.findAllByEndBeforeAndBooker_IdIsOrderByStartDesc
                        (LocalDateTime.of(2024, 1, 24, 18, 15, 30),
                                1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }
    @Test
    @Order(value = 4)
    @DisplayName("4 -findAllByStartIsAfterAndBooker_IdIsOrderByStartDesc")
    void shouldFindFourth() {
        List<Booking> expected = Collections.singletonList(firstBooking);
        List<Booking> result =
                bookingRepository.findAllByStartIsAfterAndBooker_IdIsOrderByStartDesc
                        (LocalDateTime.of(2022, 1, 24, 18, 15, 30),
                                1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }
    @Test
    @Order(value = 5)
    @DisplayName("5 - findAllByStatusAndBooker_IdIsOrderByStartDesc")
    void shouldFindFifth() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByStatusAndBooker_IdIsOrderByStartDesc
                        (Status.WAITING, 1);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }
    @Test
    @Order(value = 6)
    @DisplayName("6 - findAllByItem_Owner_IdOrderByStartDesc.")
    void shouldFindSixth() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByItem_Owner_IdOrderByStartDesc
                        (2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc.")
    void shouldFindSeventh() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByStartBeforeAndEndIsAfterAndItem_Owner_IdIsOrderByStartDesc
                        (LocalDateTime.of(2024, 1, 4, 18, 15, 30),
                                LocalDateTime.of(2024, 1, 7, 11, 50, 30),
                                2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }

    @Test
    @Order(value = 8)
    @DisplayName("8 - findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc.")
    void shouldFindEighth() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByEndBeforeAndItem_Owner_IdIsOrderByStartDesc
                        (LocalDateTime.of(2024, 1, 24, 18, 15, 30),
                                2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }

    @Test
    @Order(value = 9)
    @DisplayName("9 - findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc.")
    void shouldFindNinth() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByStartIsAfterAndItem_Owner_IdIsOrderByStartDesc
                        (LocalDateTime.of(2022, 1, 24, 18, 15, 30),
                                2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }

    @Test
    @Order(value = 10)
    @DisplayName("10 - findAllByStatusAndItem_Owner_IdIsOrderByStartDesc.")
    void shouldFindTenth() {
        List<Booking> expected = Collections.singletonList(firstBooking);

        List<Booking> result =
                bookingRepository.findAllByStatusAndItem_Owner_IdIsOrderByStartDesc
                        (Status.WAITING, 2);

        assertThat(expected.size(), equalTo(result.size()));

        assertThat(expected.get(0).getId(), equalTo(result.get(0).getId()));
        assertThat(expected.get(0).getBooker().getId(), equalTo(result.get(0).getBooker().getId()));
        assertThat(expected.get(0).getItem().getId(), equalTo(result.get(0).getItem().getId()));
        assertThat(expected.get(0).getStart(), equalTo(result.get(0).getStart()));
        assertThat(expected.get(0).getEnd(), equalTo(result.get(0).getEnd()));
        assertThat(expected.get(0).getStatus(), equalTo(result.get(0).getStatus()));
    }
    @Test
    @Order(value = 11)
    @DisplayName("11 - findFirstByItem_IdAndStartIsBeforeOrderByStartDesc.")
    void shouldFindEleven() {
        Booking expected = firstBooking;

        Optional<Booking> result =
                bookingRepository.findFirstByItem_IdAndStartIsBeforeOrderByStartDesc
                        (4, LocalDateTime.of(2024, 1, 4, 18, 15, 30));

        assertThat(expected.getId(), equalTo(result.get().getId()));
        assertThat(expected.getBooker().getId(), equalTo(result.get().getBooker().getId()));
        assertThat(expected.getItem().getId(), equalTo(result.get().getItem().getId()));
        assertThat(expected.getStart(), equalTo(result.get().getStart()));
        assertThat(expected.getEnd(), equalTo(result.get().getEnd()));
        assertThat(expected.getStatus(), equalTo(result.get().getStatus()));
    }
    @Test
    @Order(value = 12)
    @DisplayName("12 - findFirstByItem_IdAndStartIsAfterAndStatusOrderByStartAsc.")
    void shouldFindTwelve() {
        Booking expected = firstBooking;

        Optional<Booking> result =
                bookingRepository.findFirstByItem_IdAndStartIsAfterAndStatusOrderByStartAsc
                        (4, LocalDateTime.of(2022, 1, 24, 18, 15, 30),
                                Status.WAITING);

        assertThat(expected.getId(), equalTo(result.get().getId()));
        assertThat(expected.getBooker().getId(), equalTo(result.get().getBooker().getId()));
        assertThat(expected.getItem().getId(), equalTo(result.get().getItem().getId()));
        assertThat(expected.getStart(), equalTo(result.get().getStart()));
        assertThat(expected.getEnd(), equalTo(result.get().getEnd()));
        assertThat(expected.getStatus(), equalTo(result.get().getStatus()));
    }
}
