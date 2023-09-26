package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.utils.BookingServiceUtils;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceUtilsTests {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    BookingServiceUtils utils;
    private UserDto firstUserDto;
    private User firstUser;
    private UserDto secondUserDto;
    private User secondUser;
    private ItemDto firstItemDto;
    private Item firstItem;
    private BookingDto firstBookingDto;
    private Booking firstBooking;
    private BookingDto secondBookingDto;

    @BeforeAll
    void beforeAll() {
        firstUserDto = UserDto.builder()
                .id(1)
                .name("FirstUserName.")
                .email("FirstUserEmail@somemail.com")
                .build();
        firstUser = new User(1, "FirstUserName.", "FirstUserEmail@somemail.com");

        secondUserDto = UserDto.builder()
                .id(2)
                .name("SecondUserName.")
                .email("SecondUserEmail@somemail.com")
                .build();
        secondUser = new User(2, "SecondUserName.", "SecondUserEmail@somemail.com");

        firstItemDto = ItemDto.builder()
                .id(1)
                .name("FirstItemName")
                .description("FirstItemDescription")
                .available(true)
                .build();
        firstItem = new Item(1, "FirstItemName", "FirstItemDescription",
                true, secondUser, 0);
    }

    @BeforeEach
    void beforeEach() {
        firstBookingDto = BookingDto.builder()
                .id(1)
                .booker(firstUserDto)
                .itemId(1)
                .item(firstItemDto)
                .start(LocalDateTime.of(2024, 1, 2, 18, 15, 30))
                .end(LocalDateTime.of(2024, 1, 10, 11, 50, 30))
                .status(Status.WAITING.toString())
                .build();
        firstBooking = new Booking();
        firstBooking.setId(1);
        firstBooking.setItem(firstItem);
        firstBooking.setBooker(firstUser);
        firstBooking.setStart(LocalDateTime.of(2024, 1, 2, 18, 15, 30));
        firstBooking.setEnd(LocalDateTime.of(2024, 1, 10, 11, 50, 30));
        firstBooking.setStatus(Status.WAITING);


        secondBookingDto = BookingDto.builder()
                .id(2)
                .booker(secondUserDto)
                .itemId(1)
                .start(LocalDateTime.of(2024, 2, 3, 19, 16, 40))
                .end(LocalDateTime.of(2024, 2, 13, 10, 30, 20))
                .status(Status.WAITING.toString())
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should check and convert to booking.")
    void shouldCheckAndConvertToBooking() {
        when(userRepository.findById(firstUser.getId())).thenReturn(Optional.ofNullable(firstUser));
        when(itemRepository.findById(firstItem.getId())).thenReturn(Optional.ofNullable(firstItem));

        firstItem.setIsAvailable(false);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkAndConvertToBooking(firstUser.getId(), firstBookingDto));

        firstItem.setIsAvailable(true);
        firstItem.setOwner(firstUser);
        Assertions.assertThrows(NotFoundException.class,
                () -> utils.checkAndConvertToBooking(firstUser.getId(), firstBookingDto));

        firstItem.setOwner(secondUser);

        Booking result = utils.checkAndConvertToBooking(firstUser.getId(), firstBookingDto);

        assertThat(result.getStart(), equalTo(firstBookingDto.getStart()));
        assertThat(result.getEnd(), equalTo(firstBookingDto.getEnd()));
        assertThat(result.getBooker().getId(), equalTo(firstBookingDto.getBooker().getId()));
        assertThat(result.getBooker().getName(), equalTo(firstBookingDto.getBooker().getName()));
        assertThat(result.getItem().getId(), equalTo(firstBookingDto.getItem().getId()));
        assertThat(result.getItem().getName(), equalTo(firstBookingDto.getItem().getName()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should convert to Dto.")
    void shouldConvertToDto() {
        BookingDto result = utils.convertToDto(firstBooking);

        assertThat(result.getStart(), equalTo(firstBooking.getStart()));
        assertThat(result.getEnd(), equalTo(firstBooking.getEnd()));
        assertThat(result.getBooker().getId(), equalTo(firstBooking.getBooker().getId()));
        assertThat(result.getBooker().getName(), equalTo(firstBooking.getBooker().getName()));
        assertThat(result.getItem().getId(), equalTo(firstBooking.getItem().getId()));
        assertThat(result.getItem().getName(), equalTo(firstBooking.getItem().getName()));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should check if user present.")
    void shouldCheckIfUserPresent() {
        utils.checkIfUserPresent(firstUser.getId());

        Assertions.assertThrows(NotFoundException.class,
                () -> utils.checkIfUserPresent(99L));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should check is user owner.")
    void shouldCheckIsUserOwner() {
        utils.checkIsUserOwner(2, firstBooking);

        Assertions.assertThrows(NotFoundException.class,
                () -> utils.checkIsUserOwner(1, firstBooking));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should check is user booker or owner.")
    void shouldCheckIsUserBookerOrOwner() {
        utils.checkIsUserBookerOrOwner(2, firstBooking);

        Assertions.assertThrows(NotFoundException.class,
                () -> utils.checkIsUserOwner(4, firstBooking));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should check start and end time.")
    void shouldCheckStartAndEndTimes() {
        firstBooking.setStart(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));
        firstBooking.setStart(LocalDateTime.of(2024, 1, 2, 18, 15, 30));

        firstBooking.setEnd(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));
        firstBooking.setEnd(LocalDateTime.of(2024, 1, 10, 11, 50, 30));

        firstBooking.setStart(LocalDateTime.of(2020, 1, 2, 18, 15, 30));
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));
        firstBooking.setStart(LocalDateTime.of(2024, 1, 2, 18, 15, 30));

        firstBooking.setEnd(LocalDateTime.of(2020, 1, 10, 11, 50, 30));
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));
        firstBooking.setEnd(LocalDateTime.of(2024, 1, 10, 11, 50, 30));

        firstBooking.setStart(LocalDateTime.of(2024, 1, 10, 11, 50, 30));
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));

        firstBooking.setStart(LocalDateTime.of(2025, 1, 2, 18, 15, 30));
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkStartAndEndTimes(firstBooking));
    }
}
