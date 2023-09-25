package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("BookingService")
@Rollback
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceImplTests {
    private final EntityManager entityManager;
    private final BookingServiceImpl bookingService;
    private final BookingRepository bookingRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ItemServiceImpl itemService;
    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private ItemDto firstItemDto;
    private ItemDto secondItemDto;
    private ItemDto thirdItemDto;
    private ItemDto fourthItemDto;
    private ItemDto fifthItemDto;
    private BookingDto firstBookingDto;
    private BookingDto secondBookingDto;

    @BeforeAll
    void beforeAll() {
        User firstUser = new User(1, "FirstUserName", "FirstUser@somemail.com");
        User secondUser = new User(2, "SecondUserName", "SecondUser@somemail.com");
        firstUserDto = userService.addUser(UserMapper.convertToDto(firstUser));
        secondUserDto = userService.addUser(UserMapper.convertToDto(secondUser));

        firstItemDto = itemService.addItem(1, ItemMapper.convertToDto(
                new Item(1, "FirstItemName", "FirstItemDescription",
                        true, firstUser, 0)));
        secondItemDto = itemService.addItem(1, ItemMapper.convertToDto(
                new Item(2, "SecondItemName", "SecondItemDescription",
                        true, firstUser, 0)));
        thirdItemDto = itemService.addItem(1, ItemMapper.convertToDto(
                new Item(3, "ThirdItemName", "ThirdItemDescription",
                        true, firstUser, 1)));
        fourthItemDto = itemService.addItem(2, ItemMapper.convertToDto(
                new Item(4, "FourthItemName", "FourthItemDescription",
                        true, secondUser, 1)));
        fifthItemDto = itemService.addItem(2, ItemMapper.convertToDto(
                new Item(5, "FifthItemName", "FifthItemDescription",
                        true, secondUser, 1)));
    }

    @BeforeEach
    void beforeEach() {
        firstBookingDto = BookingDto.builder()
                .id(1)
                .booker(firstUserDto)
                .itemId(4)
                .start(LocalDateTime.of(2024, 1, 2, 18, 15, 30))
                .end(LocalDateTime.of(2024, 1, 10, 11, 50, 30))
                .status(Status.WAITING.toString())
                .build();
        secondBookingDto = BookingDto.builder()
                .id(2)
                .booker(secondUserDto)
                .itemId(1)
                .start(LocalDateTime.of(2024, 2, 3, 19, 16, 40))
                .end(LocalDateTime.of(2024, 2, 13, 10, 30, 20))
                .status(Status.WAITING.toString())
                .build();

        firstBookingDto = bookingService.createBooking(1, firstBookingDto);
        secondBookingDto = bookingService.createBooking(2, secondBookingDto);
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should create booking.")
    void shouldCreateBooking() {
        TypedQuery<Booking> firstBookingQuery = entityManager.createQuery
                ("Select i from Booking i where i.id = :id", Booking.class);
        Booking firstBooking = firstBookingQuery.setParameter
                ("id", firstBookingDto.getId()).getSingleResult();

        System.out.println("result = " + firstBooking + " comparing to expected = " + firstBookingDto);
        assertThat(firstBooking.getId(), equalTo(firstBookingDto.getId()));
        assertThat(firstBooking.getBooker().getId(), equalTo(firstBookingDto.getBooker().getId()));
        assertThat(firstBooking.getItem().getId(), equalTo(firstBookingDto.getItem().getId()));
        assertThat(firstBooking.getStatus().toString(), equalTo(firstBookingDto.getStatus()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - shouldSetStatus.")
    void shouldSetStatus() {
        bookingService.setStatus(2, firstBookingDto.getId(), true);

        TypedQuery<Booking> firstBookingQueryApproved = entityManager.createQuery
                ("Select i from Booking i where i.id = :id", Booking.class);
        Booking firstBookingApproved = firstBookingQueryApproved.setParameter
                ("id", firstBookingDto.getId()).getSingleResult();

        System.out.println("result = " + firstBookingApproved + " comparing to expected = " + firstBookingDto);
        assertThat(firstBookingApproved.getStatus().toString(), equalTo(Status.APPROVED.toString()));

        bookingService.setStatus(2, firstBookingDto.getId(), false);

        TypedQuery<Booking> firstBookingQueryRejected = entityManager.createQuery
                ("Select i from Booking i where i.id = :id", Booking.class);
        Booking firstBookingRejected = firstBookingQueryRejected.setParameter
                ("id", firstBookingDto.getId()).getSingleResult();

        System.out.println("result = " + firstBookingRejected + " comparing to expected = " + firstBookingDto);
        assertThat(firstBookingRejected.getStatus().toString(), equalTo(Status.REJECTED.toString()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get booking info by Id.")
    void shouldGetBookingInfoById() {

        BookingDto result = bookingService.getBookingInfo(1, firstBookingDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstBookingDto);
        assertThat(result.getId(), equalTo(firstBookingDto.getId()));
        assertThat(result.getBooker().getId(), equalTo(firstBookingDto.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(firstBookingDto.getItem().getId()));
        assertThat(result.getStatus(), equalTo(firstBookingDto.getStatus()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get user bookings.")
    void shouldGetUserBookings() {
        List<BookingDto> expected = Collections.singletonList(firstBookingDto);

        List<BookingDto> resultForAll = bookingService.getUsersBookings(1, "ALL");

        System.out.println("result = " + resultForAll + " comparing to expected = " + expected);
        assertThat(resultForAll.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForAll.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForAll.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForAll.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForCurrent = new Booking();
        bookingForCurrent.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForCurrent.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForCurrent.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForCurrent.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        bookingForCurrent.setStatus(Status.APPROVED);
        bookingForCurrent.setId(bookingRepository.save(bookingForCurrent).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForCurrent));

        List<BookingDto> resultForCurrent = bookingService.getUsersBookings(1, "CURRENT");

        System.out.println("result = " + resultForCurrent + " comparing to expected = " + expected);
        assertThat(resultForCurrent.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForCurrent.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForCurrent.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForCurrent.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForPast = new Booking();
        bookingForPast.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForPast.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForPast.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForPast.setEnd(LocalDateTime.of(2020, 2, 13, 10, 30, 20));
        bookingForPast.setStatus(Status.APPROVED);
        bookingForPast.setId(bookingRepository.save(bookingForPast).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForPast));

        List<BookingDto> resultForPast = bookingService.getUsersBookings(1, "PAST");

        System.out.println("result = " + resultForPast + " comparing to expected = " + expected);
        assertThat(resultForPast.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForPast.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForPast.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForPast.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForFuture = new Booking();
        bookingForFuture.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForFuture.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForFuture.setStart(LocalDateTime.of(2023, 12, 3, 19, 16, 40));
        bookingForFuture.setEnd(LocalDateTime.of(2023, 12, 13, 10, 30, 20));
        bookingForFuture.setStatus(Status.APPROVED);
        bookingForFuture.setId(bookingRepository.save(bookingForFuture).getId());
        expected = new ArrayList<>();
        expected.add(firstBookingDto);
        expected.add(BookingMapper.convertToDto(bookingForFuture));

        List<BookingDto> resultForFuture = bookingService.getUsersBookings(1, "FUTURE");

        System.out.println("result = " + resultForFuture + " comparing to expected = " + expected);
        assertThat(resultForFuture.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForFuture.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForFuture.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForFuture.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
        assertThat(resultForFuture.get(1).getId(), equalTo(expected.get(1).getId()));
        assertThat(resultForFuture.get(1).getBooker().getId(), equalTo(expected.get(1).getBooker().getId()));
        assertThat(resultForFuture.get(1).getItem().getId(), equalTo(expected.get(1).getItem().getId()));
        assertThat(resultForFuture.get(1).getStatus(), equalTo(expected.get(1).getStatus()));

        expected.remove(1);
        List<BookingDto> resultForWaiting = bookingService.getUsersBookings(1, "WAITING");
        System.out.println("result = " + resultForWaiting + " comparing to expected = " + expected);
        assertThat(resultForWaiting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForWaiting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForWaiting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForWaiting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking BookingForRejected = new Booking();
        BookingForRejected.setBooker(UserMapper.convertToUser(firstUserDto));
        BookingForRejected.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        BookingForRejected.setStart(LocalDateTime.of(2020, 12, 3, 19, 16, 40));
        BookingForRejected.setEnd(LocalDateTime.of(2027, 12, 13, 10, 30, 20));
        BookingForRejected.setStatus(Status.REJECTED);
        BookingForRejected.setId(bookingRepository.save(BookingForRejected).getId());
        expected = new ArrayList<>();
        expected.add(BookingMapper.convertToDto(BookingForRejected));

        List<BookingDto> resultForRejecting = bookingService.getUsersBookings(1, "REJECTED");

        System.out.println("result = " + resultForRejecting + " comparing to expected = " + expected);
        assertThat(resultForRejecting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForRejecting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForRejecting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForRejecting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should get user bookings pagination.")
    void shouldGetUserBookingsPagination() {
        List<BookingDto> expected = Collections.singletonList(firstBookingDto);

        List<BookingDto> resultForAll = bookingService.getUsersBookingsPagination(1, "ALL", 1, 10);

        System.out.println("result = " + resultForAll + " comparing to expected = " + expected);
        assertThat(resultForAll.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForAll.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForAll.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForAll.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForCurrent = new Booking();
        bookingForCurrent.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForCurrent.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForCurrent.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForCurrent.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        bookingForCurrent.setStatus(Status.APPROVED);
        bookingForCurrent.setId(bookingRepository.save(bookingForCurrent).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForCurrent));

        List<BookingDto> resultForCurrent = bookingService.getUsersBookingsPagination(1, "CURRENT", 1, 10);

        System.out.println("result = " + resultForCurrent + " comparing to expected = " + expected);
        assertThat(resultForCurrent.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForCurrent.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForCurrent.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForCurrent.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForPast = new Booking();
        bookingForPast.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForPast.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForPast.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForPast.setEnd(LocalDateTime.of(2020, 2, 13, 10, 30, 20));
        bookingForPast.setStatus(Status.APPROVED);
        bookingForPast.setId(bookingRepository.save(bookingForPast).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForPast));

        List<BookingDto> resultForPast = bookingService.getUsersBookingsPagination(1, "PAST", 1, 10);

        System.out.println("result = " + resultForPast + " comparing to expected = " + expected);
        assertThat(resultForPast.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForPast.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForPast.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForPast.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForFuture = new Booking();
        bookingForFuture.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForFuture.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForFuture.setStart(LocalDateTime.of(2023, 12, 3, 19, 16, 40));
        bookingForFuture.setEnd(LocalDateTime.of(2023, 12, 13, 10, 30, 20));
        bookingForFuture.setStatus(Status.APPROVED);
        bookingForFuture.setId(bookingRepository.save(bookingForFuture).getId());
        expected = new ArrayList<>();
        expected.add(firstBookingDto);
        expected.add(BookingMapper.convertToDto(bookingForFuture));

        List<BookingDto> resultForFuture = bookingService.getUsersBookingsPagination(1, "FUTURE", 1, 10);

        System.out.println("result = " + resultForFuture + " comparing to expected = " + expected);
        assertThat(resultForFuture.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForFuture.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForFuture.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForFuture.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
        assertThat(resultForFuture.get(1).getId(), equalTo(expected.get(1).getId()));
        assertThat(resultForFuture.get(1).getBooker().getId(), equalTo(expected.get(1).getBooker().getId()));
        assertThat(resultForFuture.get(1).getItem().getId(), equalTo(expected.get(1).getItem().getId()));
        assertThat(resultForFuture.get(1).getStatus(), equalTo(expected.get(1).getStatus()));

        expected.remove(1);
        List<BookingDto> resultForWaiting = bookingService.getUsersBookingsPagination(1, "WAITING", 1, 10);
        System.out.println("result = " + resultForWaiting + " comparing to expected = " + expected);
        assertThat(resultForWaiting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForWaiting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForWaiting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForWaiting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking BookingForRejected = new Booking();
        BookingForRejected.setBooker(UserMapper.convertToUser(firstUserDto));
        BookingForRejected.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        BookingForRejected.setStart(LocalDateTime.of(2020, 12, 3, 19, 16, 40));
        BookingForRejected.setEnd(LocalDateTime.of(2027, 12, 13, 10, 30, 20));
        BookingForRejected.setStatus(Status.REJECTED);
        BookingForRejected.setId(bookingRepository.save(BookingForRejected).getId());
        expected = new ArrayList<>();
        expected.add(BookingMapper.convertToDto(BookingForRejected));

        List<BookingDto> resultForRejecting = bookingService.getUsersBookingsPagination(1, "REJECTED", 1, 10);

        System.out.println("result = " + resultForRejecting + " comparing to expected = " + expected);
        assertThat(resultForRejecting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForRejecting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForRejecting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForRejecting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should get user's item's bookings.")
    void ShouldGetUsersItemsBookings() {
        List<BookingDto> expected = Collections.singletonList(firstBookingDto);

        List<BookingDto> result = bookingService.getUsersItemsBookings(2, "ALL");

        System.out.println("result = " + result + " comparing to expected = " + expected);
        assertThat(result.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(result.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(result.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(result.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForCurrent = new Booking();
        bookingForCurrent.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForCurrent.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForCurrent.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForCurrent.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        bookingForCurrent.setStatus(Status.APPROVED);
        bookingForCurrent.setId(bookingRepository.save(bookingForCurrent).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForCurrent));

        List<BookingDto> resultForCurrent = bookingService.getUsersItemsBookings(2, "CURRENT");

        System.out.println("result = " + resultForCurrent + " comparing to expected = " + expected);
        assertThat(resultForCurrent.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForCurrent.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForCurrent.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForCurrent.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForPast = new Booking();
        bookingForPast.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForPast.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForPast.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForPast.setEnd(LocalDateTime.of(2020, 2, 13, 10, 30, 20));
        bookingForPast.setStatus(Status.APPROVED);
        bookingForPast.setId(bookingRepository.save(bookingForPast).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForPast));

        List<BookingDto> resultForPast = bookingService.getUsersItemsBookings(2, "PAST");

        System.out.println("result = " + resultForPast + " comparing to expected = " + expected);
        assertThat(resultForPast.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForPast.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForPast.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForPast.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForFuture = new Booking();
        bookingForFuture.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForFuture.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForFuture.setStart(LocalDateTime.of(2023, 12, 3, 19, 16, 40));
        bookingForFuture.setEnd(LocalDateTime.of(2023, 12, 13, 10, 30, 20));
        bookingForFuture.setStatus(Status.APPROVED);
        bookingForFuture.setId(bookingRepository.save(bookingForFuture).getId());
        expected = new ArrayList<>();
        expected.add(firstBookingDto);
        expected.add(BookingMapper.convertToDto(bookingForFuture));

        List<BookingDto> resultForFuture = bookingService.getUsersItemsBookings(2, "FUTURE");

        System.out.println("result = " + resultForFuture + " comparing to expected = " + expected);
        assertThat(resultForFuture.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForFuture.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForFuture.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForFuture.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
        assertThat(resultForFuture.get(1).getId(), equalTo(expected.get(1).getId()));
        assertThat(resultForFuture.get(1).getBooker().getId(), equalTo(expected.get(1).getBooker().getId()));
        assertThat(resultForFuture.get(1).getItem().getId(), equalTo(expected.get(1).getItem().getId()));
        assertThat(resultForFuture.get(1).getStatus(), equalTo(expected.get(1).getStatus()));

        expected.remove(1);
        List<BookingDto> resultForWaiting = bookingService.getUsersItemsBookings(2, "WAITING");
        System.out.println("result = " + resultForWaiting + " comparing to expected = " + expected);
        assertThat(resultForWaiting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForWaiting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForWaiting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForWaiting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking BookingForRejected = new Booking();
        BookingForRejected.setBooker(UserMapper.convertToUser(firstUserDto));
        BookingForRejected.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        BookingForRejected.setStart(LocalDateTime.of(2020, 12, 3, 19, 16, 40));
        BookingForRejected.setEnd(LocalDateTime.of(2027, 12, 13, 10, 30, 20));
        BookingForRejected.setStatus(Status.REJECTED);
        BookingForRejected.setId(bookingRepository.save(BookingForRejected).getId());
        expected = new ArrayList<>();
        expected.add(BookingMapper.convertToDto(BookingForRejected));

        List<BookingDto> resultForRejecting = bookingService.getUsersItemsBookings(2, "REJECTED");

        System.out.println("result = " + resultForRejecting + " comparing to expected = " + expected);
        assertThat(resultForRejecting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForRejecting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForRejecting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForRejecting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - should get user's item's bookings pagination.")
    void ShouldGetUsersItemsBookingsPagination() {
        List<BookingDto> expected = Collections.singletonList(firstBookingDto);

        List<BookingDto> result = bookingService.getUsersItemsBookingsPagination(2, "ALL", 1, 10);

        System.out.println("result = " + result + " comparing to expected = " + expected);
        assertThat(result.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(result.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(result.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(result.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForCurrent = new Booking();
        bookingForCurrent.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForCurrent.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForCurrent.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForCurrent.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        bookingForCurrent.setStatus(Status.APPROVED);
        bookingForCurrent.setId(bookingRepository.save(bookingForCurrent).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForCurrent));

        List<BookingDto> resultForCurrent = bookingService.getUsersItemsBookingsPagination(2, "CURRENT", 1, 10);

        System.out.println("result = " + resultForCurrent + " comparing to expected = " + expected);
        assertThat(resultForCurrent.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForCurrent.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForCurrent.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForCurrent.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForPast = new Booking();
        bookingForPast.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForPast.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForPast.setStart(LocalDateTime.of(2020, 2, 3, 19, 16, 40));
        bookingForPast.setEnd(LocalDateTime.of(2020, 2, 13, 10, 30, 20));
        bookingForPast.setStatus(Status.APPROVED);
        bookingForPast.setId(bookingRepository.save(bookingForPast).getId());
        expected = Collections.singletonList(BookingMapper.convertToDto(bookingForPast));

        List<BookingDto> resultForPast = bookingService.getUsersItemsBookingsPagination(2, "PAST", 1, 10);

        System.out.println("result = " + resultForPast + " comparing to expected = " + expected);
        assertThat(resultForPast.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForPast.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForPast.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForPast.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking bookingForFuture = new Booking();
        bookingForFuture.setBooker(UserMapper.convertToUser(firstUserDto));
        bookingForFuture.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        bookingForFuture.setStart(LocalDateTime.of(2023, 12, 3, 19, 16, 40));
        bookingForFuture.setEnd(LocalDateTime.of(2023, 12, 13, 10, 30, 20));
        bookingForFuture.setStatus(Status.APPROVED);
        bookingForFuture.setId(bookingRepository.save(bookingForFuture).getId());
        expected = new ArrayList<>();
        expected.add(firstBookingDto);
        expected.add(BookingMapper.convertToDto(bookingForFuture));

        List<BookingDto> resultForFuture = bookingService.getUsersItemsBookingsPagination(2, "FUTURE", 1, 10);

        System.out.println("result = " + resultForFuture + " comparing to expected = " + expected);
        assertThat(resultForFuture.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForFuture.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForFuture.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForFuture.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
        assertThat(resultForFuture.get(1).getId(), equalTo(expected.get(1).getId()));
        assertThat(resultForFuture.get(1).getBooker().getId(), equalTo(expected.get(1).getBooker().getId()));
        assertThat(resultForFuture.get(1).getItem().getId(), equalTo(expected.get(1).getItem().getId()));
        assertThat(resultForFuture.get(1).getStatus(), equalTo(expected.get(1).getStatus()));

        expected.remove(1);
        List<BookingDto> resultForWaiting = bookingService.getUsersItemsBookingsPagination(2, "WAITING", 1, 10);
        System.out.println("result = " + resultForWaiting + " comparing to expected = " + expected);
        assertThat(resultForWaiting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForWaiting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForWaiting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForWaiting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));

        Booking BookingForRejected = new Booking();
        BookingForRejected.setBooker(UserMapper.convertToUser(firstUserDto));
        BookingForRejected.setItem(ItemMapper.convertToItem(fourthItemDto, UserMapper.convertToUser(secondUserDto)));
        BookingForRejected.setStart(LocalDateTime.of(2020, 12, 3, 19, 16, 40));
        BookingForRejected.setEnd(LocalDateTime.of(2027, 12, 13, 10, 30, 20));
        BookingForRejected.setStatus(Status.REJECTED);
        BookingForRejected.setId(bookingRepository.save(BookingForRejected).getId());
        expected = new ArrayList<>();
        expected.add(BookingMapper.convertToDto(BookingForRejected));

        List<BookingDto> resultForRejecting = bookingService.getUsersItemsBookingsPagination(2, "REJECTED", 1, 10);

        System.out.println("result = " + resultForRejecting + " comparing to expected = " + expected);
        assertThat(resultForRejecting.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(resultForRejecting.get(0).getBooker().getId(), equalTo(expected.get(0).getBooker().getId()));
        assertThat(resultForRejecting.get(0).getItem().getId(), equalTo(expected.get(0).getItem().getId()));
        assertThat(resultForRejecting.get(0).getStatus(), equalTo(expected.get(0).getStatus()));
    }
}
