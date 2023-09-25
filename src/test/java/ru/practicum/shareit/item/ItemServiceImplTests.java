package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Comment.CommentRepository;
import ru.practicum.shareit.item.Comment.model.Comment;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.service.utils.ItemServiceUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ItemService")
public class ItemServiceImplTests {

    ItemRepository itemRepository;

    BookingService bookingService;

    CommentRepository commentRepository;

    UserService userService;

    ItemServiceUtils utils;

    ItemServiceImpl itemService;

    UserDto firstUserDto;
    User firstUser;
    ItemDto firstItemDto;
    ItemDto secondItemDto;
    Item firstItem;
    Item secondItem;


    @BeforeEach
    void beforeEach() {
        this.itemRepository = Mockito.mock(ItemRepository.class);
        this.bookingService = Mockito.mock(BookingService.class);
        this.commentRepository = Mockito.mock(CommentRepository.class);
        this.userService = Mockito.mock(UserService.class);
        this.utils = Mockito.mock(ItemServiceUtils.class);

        itemService = new ItemServiceImpl(
                this.itemRepository,
                this.bookingService,
                this.commentRepository,
                this.userService,
                this.utils
        );

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

        secondItemDto = ItemDto.builder()
                .name("SecondItemName")
                .description("SecondItemDescription")
                .available(true)
                .build();
        secondItem = new Item(2, "SecondItemName", "SecondItemDescription",
                true, firstUser, 0);
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should add item.")
    void shouldAddItem() {
        when(userService.getUserById(firstUserDto.getId())).thenReturn(firstUser);
        when(utils.convertToItem(firstItemDto, firstUser)).thenReturn(firstItem);
        when(itemRepository.save(firstItem)).thenReturn(firstItem);
        when(utils.convertToDto(firstItem)).thenReturn(firstItemDto);

        ItemDto result = itemService.addItem(firstUserDto.getId(), firstItemDto);

        System.out.println("result = " + result + " comparing to expected = " + firstItem);
        assertThat(result.getId(), equalTo(firstItem.getId()));
        assertThat(result.getName(), equalTo(firstItem.getName()));
        assertThat(result.getDescription(), equalTo(firstItem.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItem.getIsAvailable()));
        assertThat(result.getId(), equalTo(firstItem.getOwner().getId()));
        assertThat(result.getRequestId(), equalTo(firstItem.getRequest()));

        verify(userService, times(1)).getUserById(firstUserDto.getId());
        verify(utils, times(1)).convertToItem(firstItemDto, firstUser);
        verify(itemRepository, times(1)).save(firstItem);
        verify(utils, times(1)).convertToDto(firstItem);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update item.")
    void shouldUpdateItem() {
        when(userService.getUserById(firstUserDto.getId())).thenReturn(firstUser);
        when(utils.convertToItem(firstItemDto, firstUser)).thenReturn(firstItem);
        when(itemRepository.save(firstItem)).thenReturn(firstItem);
        when(utils.convertToDto(firstItem)).thenReturn(firstItemDto);

        System.out.println(itemService);
        System.out.println(firstItemDto);
        System.out.println(firstUserDto);

        firstItemDto = itemService.addItem(firstUserDto.getId(), firstItemDto);

        when(itemRepository.findById(firstItemDto.getId())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingService.getLastBookingForItem(anyLong())).thenReturn(null);
        when(bookingService.getNextBookingForItem(anyLong())).thenReturn(null);
        when(commentRepository.findAllByItem_IdOrderByIdDesc(anyLong())).thenReturn(new ArrayList<>());

        firstItemDto.setName("UpdatedName.");
        firstItemDto.setDescription("UpdatedDescription.");
        firstItemDto.setAvailable(false);
        firstItemDto.setRequestId(123);

        ItemDto result = itemService.updateItem(firstUserDto.getId(), firstItemDto);

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItemDto.getAvailable()));
        assertThat(result.getRequestId(), equalTo(firstItemDto.getRequestId()));

        verify(itemRepository, times(2)).findById(firstItemDto.getId());
        verify(bookingService, times(1)).getLastBookingForItem(anyLong());
        verify(bookingService, times(1)).getNextBookingForItem(anyLong());
        verify(commentRepository, times(1)).findAllByItem_IdOrderByIdDesc(anyLong());
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get item DTO by id.")
    void shouldGetItemDtoById() {
        when(utils.convertToDto(firstItem)).thenReturn(firstItemDto);
        when(itemRepository.findById(firstItemDto.getId())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingService.getLastBookingForItem(anyLong())).thenReturn(null);
        when(bookingService.getNextBookingForItem(anyLong())).thenReturn(null);
        when(commentRepository.findAllByItem_IdOrderByIdDesc(anyLong())).thenReturn(new ArrayList<>());

        ItemDto result = itemService.getItemDtoById(firstUserDto.getId(), firstItemDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItemDto.getAvailable()));
        assertThat(result.getRequestId(), equalTo(firstItemDto.getRequestId()));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get item by id.")
    void shouldGetItemById() {
        when(itemRepository.findById(firstItemDto.getId())).thenReturn(Optional.ofNullable(firstItem));

        Item result = itemService.getItemById(firstUserDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getIsAvailable(), equalTo(firstItemDto.getAvailable()));
        assertThat(result.getRequest(), equalTo(firstItemDto.getRequestId()));

        verify(itemRepository, times(1)).findById(firstItemDto.getId());
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should get all user's items.")
    void shouldGetAllUserItems() {
        List<Item> correctResult = new ArrayList<>();
        correctResult.add(firstItem);
        correctResult.add(secondItem);
        when(itemRepository.findAllByOwner_IdOrderByIdAsc(firstUserDto.getId()))
                .thenReturn(correctResult);
        when(bookingService.getLastBookingForItem(anyLong())).thenReturn(null);
        when(bookingService.getNextBookingForItem(anyLong())).thenReturn(null);
        when(commentRepository.findAllByItem_IdOrderByIdDesc(anyLong())).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.getItemsByUserId(firstUserDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + correctResult);
        assertThat(result.size(), equalTo(correctResult.size()));

        assertThat(result.get(0).getId(), equalTo(correctResult.get(0).getId()));
        assertThat(result.get(0).getName(), equalTo(correctResult.get(0).getName()));
        assertThat(result.get(0).getDescription(), equalTo(correctResult.get(0).getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(correctResult.get(0).getIsAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(correctResult.get(0).getRequest()));

        assertThat(result.get(1).getId(), equalTo(correctResult.get(1).getId()));
        assertThat(result.get(1).getName(), equalTo(correctResult.get(1).getName()));
        assertThat(result.get(1).getDescription(), equalTo(correctResult.get(1).getDescription()));
        assertThat(result.get(1).getAvailable(), equalTo(correctResult.get(1).getIsAvailable()));
        assertThat(result.get(1).getRequestId(), equalTo(correctResult.get(1).getRequest()));
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should get all user's items pagination.")
    void shouldGetAllUserItemsPagination() {
        List<Item> correctResult = new ArrayList<>();
        correctResult.add(firstItem);
        correctResult.add(secondItem);
        when(itemRepository.findAllByOwner_IdOrderByIdAsc(firstUserDto.getId(), PageRequest.of(1 / 10, 10)))
                .thenReturn(new PageImpl<>(correctResult));
        when(bookingService.getLastBookingForItem(anyLong())).thenReturn(null);
        when(bookingService.getNextBookingForItem(anyLong())).thenReturn(null);
        when(commentRepository.findAllByItem_IdOrderByIdDesc(anyLong())).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.getItemsByUserIdPagination(firstUserDto.getId(), 1, 10);

        System.out.println("result = " + result + " comparing to expected = " + correctResult);
        assertThat(result.size(), equalTo(correctResult.size()));

        assertThat(result.get(0).getId(), equalTo(correctResult.get(0).getId()));
        assertThat(result.get(0).getName(), equalTo(correctResult.get(0).getName()));
        assertThat(result.get(0).getDescription(), equalTo(correctResult.get(0).getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(correctResult.get(0).getIsAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(correctResult.get(0).getRequest()));

        assertThat(result.get(1).getId(), equalTo(correctResult.get(1).getId()));
        assertThat(result.get(1).getName(), equalTo(correctResult.get(1).getName()));
        assertThat(result.get(1).getDescription(), equalTo(correctResult.get(1).getDescription()));
        assertThat(result.get(1).getAvailable(), equalTo(correctResult.get(1).getIsAvailable()));
        assertThat(result.get(1).getRequestId(), equalTo(correctResult.get(1).getRequest()));
    }

    @Test
    @Order(value = 7)
    @DisplayName("7 - should search in description.")
    void shouldSearchInDescription() {
        List<Item> correctResult = new ArrayList<>();
        correctResult.add(secondItem);
        when(itemRepository.findAllByDescriptionContainsIgnoreCase("cond"))
                .thenReturn(correctResult);

        List<ItemDto> result = itemService.searchInDescription("cond");

        System.out.println("result = " + result + " comparing to expected = " + correctResult);
        assertThat(result.size(), equalTo(correctResult.size()));

        assertThat(result.get(0).getId(), equalTo(correctResult.get(0).getId()));
        assertThat(result.get(0).getName(), equalTo(correctResult.get(0).getName()));
        assertThat(result.get(0).getDescription(), equalTo(correctResult.get(0).getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(correctResult.get(0).getIsAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(correctResult.get(0).getRequest()));
    }

    @Test
    @Order(value = 8)
    @DisplayName("8 - should search in description pagination.")
    void shouldSearchInDescriptionPagination() {
        List<Item> correctResult = new ArrayList<>();
        correctResult.add(secondItem);
        when(itemRepository.findAllByDescriptionContainsIgnoreCase(("cond"), PageRequest.of(1 / 10, 10)))
                .thenReturn(new PageImpl<>(correctResult));

        List<ItemDto> result = itemService.searchInDescriptionPagination("cond", 1, 10);

        System.out.println("result = " + result + " comparing to expected = " + correctResult);
        assertThat(result.size(), equalTo(correctResult.size()));

        assertThat(result.get(0).getId(), equalTo(correctResult.get(0).getId()));
        assertThat(result.get(0).getName(), equalTo(correctResult.get(0).getName()));
        assertThat(result.get(0).getDescription(), equalTo(correctResult.get(0).getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(correctResult.get(0).getIsAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(correctResult.get(0).getRequest()));
    }

    @Test
    @Order(value = 9)
    @DisplayName("9 - should get items by request.")
    void shouldGetItemsByRequest() {
        secondItem.setRequest(9);
        List<Item> correctResult = new ArrayList<>();
        correctResult.add(secondItem);
        when(itemRepository.findAllByRequest(9))
                .thenReturn(correctResult);

        List<ItemDto> result = itemService.getItemsForRequest(9);

        System.out.println("result = " + result + " comparing to expected = " + correctResult);
        assertThat(result.size(), equalTo(correctResult.size()));

        assertThat(result.get(0).getId(), equalTo(correctResult.get(0).getId()));
        assertThat(result.get(0).getName(), equalTo(correctResult.get(0).getName()));
        assertThat(result.get(0).getDescription(), equalTo(correctResult.get(0).getDescription()));
        assertThat(result.get(0).getAvailable(), equalTo(correctResult.get(0).getIsAvailable()));
        assertThat(result.get(0).getRequestId(), equalTo(correctResult.get(0).getRequest()));
    }

    @Test
    @Order(value = 10)
    @DisplayName("10 - should add comment.")
    void shouldAddComment() {
        CommentDto firstCommentDto = CommentDto.builder()
                .id(1)
                .text("first comment text.")
                .authorName("first user.")
                .itemName("first item.")
                .build();
        Comment firstComment = new Comment(firstCommentDto.getId(), firstCommentDto.getText(),
                firstItem, firstUser, LocalDateTime.now());

        when(itemRepository.findById(firstItem.getId())).thenReturn(Optional.ofNullable(firstItem));
        when(utils.createComment(firstCommentDto, firstUserDto.getId(), firstItem))
                .thenReturn(firstComment);
        when(commentRepository.save(firstComment)).thenReturn(firstComment);
        when(utils.convertToDto(firstComment)).thenReturn(firstCommentDto);

        CommentDto result = itemService.addComment(firstUserDto.getId(), firstItemDto.getId(), firstCommentDto);

        System.out.println("result = " + result + " comparing to expected = " + firstCommentDto);
        assertThat(result.getId(), equalTo(firstCommentDto.getId()));
        assertThat(result.getText(), equalTo(firstCommentDto.getText()));
        assertThat(result.getItemName(), equalTo(firstCommentDto.getItemName()));
        assertThat(result.getAuthorName(), equalTo(firstCommentDto.getAuthorName()));
    }

    @Test
    @Order(value = 11)
    @DisplayName("11 - comments and bookings.")
    void shouldReturnWithCommentsAndBookings() {
        Comment firstComment = new Comment(1, "first comment text.",
                firstItem, firstUser, LocalDateTime.now());
        Comment secondComment = new Comment(2, "second comment text.",
                firstItem, firstUser, LocalDateTime.now());
        List<Comment> comments = new ArrayList<>();
        comments.add(firstComment);
        comments.add(secondComment);

        Booking expectingLastBooking = new Booking();
        expectingLastBooking.setId(1);
        expectingLastBooking.setBooker(firstUser);
        expectingLastBooking.setItem(firstItem);
        expectingLastBooking.setStart(LocalDateTime.of(2019, 1, 2, 18, 15, 30));
        expectingLastBooking.setEnd(LocalDateTime.of(2020, 1, 10, 11, 50, 30));
        expectingLastBooking.setStatus(Status.APPROVED);

        Booking expectingNextBooking = new Booking();
        expectingNextBooking.setId(2);
        expectingNextBooking.setBooker(firstUser);
        expectingNextBooking.setItem(firstItem);
        expectingNextBooking.setStart(LocalDateTime.of(2024, 2, 3, 19, 16, 40));
        expectingNextBooking.setEnd(LocalDateTime.of(2024, 2, 13, 10, 30, 20));
        expectingNextBooking.setStatus(Status.WAITING);


        when(utils.convertToDto(firstItem)).thenReturn(firstItemDto);
        when(itemRepository.findById(firstItemDto.getId())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingService.getLastBookingForItem(anyLong())).thenReturn(expectingLastBooking);
        when(bookingService.getNextBookingForItem(anyLong())).thenReturn(expectingNextBooking);
        when(commentRepository.findAllByItem_IdOrderByIdDesc(anyLong())).thenReturn(comments);

        ItemDto result = itemService.getItemDtoById(firstUserDto.getId(), firstItemDto.getId());

        System.out.println("result = " + result + " comparing to expected = " + firstItemDto);
        assertThat(result.getId(), equalTo(firstItemDto.getId()));
        assertThat(result.getName(), equalTo(firstItemDto.getName()));
        assertThat(result.getDescription(), equalTo(firstItemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(firstItemDto.getAvailable()));
        assertThat(result.getRequestId(), equalTo(firstItemDto.getRequestId()));

        assertThat(result.getComments().size(), equalTo(comments.size()));

        assertThat(result.getComments().get(0).getText(), equalTo(comments.get(0).getText()));
        assertThat(result.getComments().get(0).getItemName(), equalTo(comments.get(0).getItem().getName()));
        assertThat(result.getComments().get(0).getAuthorName(), equalTo(comments.get(0).getAuthor().getName()));
        assertThat(result.getComments().get(0).getId(), equalTo(comments.get(0).getId()));

        assertThat(result.getComments().get(1).getText(), equalTo(comments.get(1).getText()));
        assertThat(result.getComments().get(1).getItemName(), equalTo(comments.get(1).getItem().getName()));
        assertThat(result.getComments().get(1).getAuthorName(), equalTo(comments.get(1).getAuthor().getName()));
        assertThat(result.getComments().get(1).getId(), equalTo(comments.get(1).getId()));

        assertThat(result.getLastBooking().getBookerId(), equalTo(expectingLastBooking.getBooker().getId()));
        assertThat(result.getLastBooking().getId(), equalTo(expectingLastBooking.getId()));

        assertThat(result.getNextBooking().getBookerId(), equalTo(expectingNextBooking.getBooker().getId()));
        assertThat(result.getNextBooking().getId(), equalTo(expectingNextBooking.getId()));
    }
}
