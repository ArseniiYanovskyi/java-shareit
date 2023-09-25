package ru.practicum.shareit.request;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.utils.ItemRequestServiceUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRequestServiceUtilsTests {
    UserService userService;
    ItemService itemService;
    ItemRequestServiceUtils utils;
    private ItemRequest firstItemRequest;
    private ItemRequestDto firstItemRequestDto;
    private Item firstItem;
    private Item secondItem;
    private User firstUser;
    private User secondUser;
    private List<Item> itemsForRequest;
    private List<ItemDto> itemDtoForRequest;

    @BeforeEach
    void beforeEach() {
        this.userService = Mockito.mock(UserServiceImpl.class);
        this.itemService = Mockito.mock(ItemServiceImpl.class);
        this.utils = new ItemRequestServiceUtils(userService, itemService);

        firstUser = new User(1L, "FirstUser", "FirstUser@somemail.com");
        secondUser = new User(2L, "SecondUser", "SecondUser@somemail.com");
        firstItem = new Item(1L, "FirstItemName", "FirstItemDescription",
                true, firstUser, 1);
        secondItem = new Item(2L, "SecondItemName", "SecondItemDescription",
                true, secondUser, 1);

        itemsForRequest = new ArrayList<>();
        itemsForRequest.add(firstItem);
        itemsForRequest.add(secondItem);

        itemDtoForRequest = itemsForRequest.stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());

        firstItemRequest = new ItemRequest();
        firstItemRequest.setId(1);
        firstItemRequest.setDescription("FirstItemRequestDescription.");
        firstItemRequest.setPublisher(1);
        firstItemRequest.setCreationDate(LocalDateTime.of
                (2023, 9, 22, 18, 30, 50));

        firstItemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("FirstItemRequestDescription.")
                .created(LocalDateTime.of
                        (2023, 9, 22, 18, 30, 50))
                .items(itemDtoForRequest)
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should check and convert to request.")
    void shouldCheckAndConvertToRequest() {
        ItemRequest result = utils.checkAndConvertToRequest(firstUser.getId(), firstItemRequestDto);

        assertThat(result.getDescription(), equalTo(firstItemRequestDto.getDescription()));
        assertThat(result.getId(), equalTo(firstItemRequestDto.getId()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should convert to dto.")
    void shouldConvertToDto() {
        System.out.println(itemDtoForRequest);
        when(itemService.getItemsForRequest(anyLong())).thenReturn(itemDtoForRequest);

        ItemRequestDto result = utils.convertToDto(firstItemRequest);

        assertThat(result.getDescription(), equalTo(firstItemRequestDto.getDescription()));
        assertThat(result.getId(), equalTo(firstItemRequestDto.getId()));

        assertThat(result.getItems().size(), equalTo(itemDtoForRequest.size()));
        assertThat(result.getItems().get(0).getName(), equalTo(itemDtoForRequest.get(0).getName()));
        assertThat(result.getItems().get(0).getDescription(), equalTo(itemDtoForRequest.get(0).getDescription()));
        assertThat(result.getItems().get(1).getName(), equalTo(itemDtoForRequest.get(1).getName()));
        assertThat(result.getItems().get(1).getDescription(), equalTo(itemDtoForRequest.get(1).getDescription()));
    }
}
