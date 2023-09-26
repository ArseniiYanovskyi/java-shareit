package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.Comment.model.Comment;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.utils.ItemServiceUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceUtilsTests {
    @Mock
    BookingService bookingService;
    @Mock
    UserService userService;
    @InjectMocks
    ItemServiceUtils utils;

    private UserDto firstUserDto;
    private User firstUser;
    private ItemDto firstItemDto;
    private Item firstItem;
    private BookingDto firstBookingDto;

    @BeforeEach
    void beforeEach() {
        firstUserDto = UserDto.builder()
                .id(1)
                .name("FirstUserName.")
                .email("FirstUserEmail@somemail.com")
                .build();
        firstUser = new User(1, "FirstUserName.", "FirstUserEmail@somemail.com");

        firstItemDto = ItemDto.builder()
                .id(1)
                .name("FirstItemName")
                .description("FirstItemDescription")
                .available(true)
                .build();
        firstItem = new Item(1, "FirstItemName", "FirstItemDescription",
                true, firstUser, 0);

        firstBookingDto = BookingDto.builder()
                .id(1)
                .booker(firstUserDto)
                .itemId(1)
                .item(firstItemDto)
                .start(LocalDateTime.of(2021, 1, 2, 18, 15, 30))
                .end(LocalDateTime.of(2022, 1, 10, 11, 50, 30))
                .status(Status.APPROVED.toString())
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should check itemDto validation.")
    void shouldCheckItemDtoValidation() {
        firstItemDto.setName(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkItemDtoValidation(firstItemDto));
        firstItemDto.setName("");
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkItemDtoValidation(firstItemDto));
        firstItemDto.setName("FirstItemName");
        firstItemDto.setDescription(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkItemDtoValidation(firstItemDto));
        firstItemDto.setDescription("");
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkItemDtoValidation(firstItemDto));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should check is item available.")
    void shouldCheckIsItemAvailable() {
        firstItemDto.setAvailable(null);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkIsItemAvailable(firstItemDto));
        firstItemDto.setAvailable(false);
        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkIsItemAvailable(firstItemDto));
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should convert to Dto.")
    void shouldConvertToDto() {
        ItemDto result = utils.convertToDto(firstItem);

        assertThat(result.getId(), equalTo(firstItem.getId()));
        assertThat(result.getDescription(), equalTo(firstItem.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItem.getIsAvailable()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should convert to item.")
    void shouldConvertToItem() {
        Item result = utils.convertToItem(firstItemDto, firstUser);

        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getIsAvailable(), equalTo(firstItemDto.getAvailable()));
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should check if user rented item.")
    void shouldCheckIfUserRentedItem() {
        when(bookingService.getUsersBookings(firstUser.getId(), "ALL"))
                .thenReturn(Collections.singletonList(firstBookingDto));

        utils.checkIfUserRentedItem(firstUser.getId(), firstItem.getId());
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should throw if user did not rent item.")
    void shouldThrowIfUserWasNotRented() {
        when(bookingService.getUsersBookings(firstUser.getId(), "ALL"))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(ValidationException.class,
                () -> utils.checkIfUserRentedItem(firstUser.getId(), firstItem.getId()));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - should create comment.")
    void shouldCreateComment() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .itemName("FirstItemName")
                .authorName("FirstUserName.")
                .text("my comment.")
                .build();

        when(userService.getUserById(firstUserDto.getId())).thenReturn(firstUser);

        Comment result = utils.createComment(commentDto, firstUserDto.getId(), firstItem);

        assertThat(result.getText(), equalTo(commentDto.getText()));
        assertThat(result.getAuthor().getId(), equalTo(firstUser.getId()));
        assertThat(result.getAuthor().getName(), equalTo(firstUser.getName()));
        assertThat(result.getItem().getId(), equalTo(firstItem.getId()));
        assertThat(result.getItem().getName(), equalTo(firstItem.getName()));
    }

    @Test
    @Order(value = 8)
    @DisplayName("8 - should create comment.")
    void shouldConvertCommentToDto() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .itemName("FirstItemName")
                .authorName("FirstUserName.")
                .text("my comment.")
                .build();

        when(userService.getUserById(firstUserDto.getId())).thenReturn(firstUser);

        Comment comment = utils.createComment(commentDto, firstUserDto.getId(), firstItem);

        CommentDto result = utils.convertToDto(comment);

        assertThat(result.getText(), equalTo(commentDto.getText()));
        assertThat(result.getAuthorName(), equalTo(commentDto.getAuthorName()));
        assertThat(result.getItemName(), equalTo(commentDto.getItemName()));
    }

    @Test
    @Order(value = 9)
    @DisplayName("9 - should throw when comment text empty.")
    void shouldThrowWhenCommentTextEmpty() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .itemName("FirstItemName")
                .authorName("FirstUserName.")
                .text(null)
                .build();
        Assertions.assertThrows(ValidationException.class,
                () -> utils.createComment(commentDto, firstUserDto.getId(), firstItem));

        commentDto.setText("");
        Assertions.assertThrows(ValidationException.class,
                () -> utils.createComment(commentDto, firstUserDto.getId(), firstItem));
    }
}
