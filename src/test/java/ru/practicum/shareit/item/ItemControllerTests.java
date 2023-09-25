package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Comment.model.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ItemController")
@WebMvcTest(controllers = ItemController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class ItemControllerTests {
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private ItemDto firstItem;
    private ItemDto secondItem;
    private ItemDto thirdItem;

    @BeforeEach
    void beforeEach() {
        firstItem = ItemDto.builder()
                .id(1)
                .name("FirstItemName")
                .description("FirstItemDescription")
                .available(true)
                .build();
        secondItem = ItemDto.builder()
                .id(2)
                .name("SecondItemName")
                .description("SecondItemDescription")
                .available(true)
                .build();
        thirdItem = ItemDto.builder()
                .id(3)
                .name("ThirdItemName")
                .description("ThirdItemDescription")
                .available(true)
                .requestId(1)
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should add new items.")
    void shouldAddNewItems() throws Exception {

        when(itemService.addItem(1, firstItem))
                .thenReturn(firstItem);

        mockMvc.perform(post("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(firstItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstItem.getName())))
                .andExpect(jsonPath("$.description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.available", is(firstItem.getAvailable())));


        when(itemService.addItem(1, secondItem))
                .thenReturn(secondItem);

        mockMvc.perform(post("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(secondItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is(secondItem.getName())))
                .andExpect(jsonPath("$.description", is(secondItem.getDescription())))
                .andExpect(jsonPath("$.available", is(secondItem.getAvailable())));


        when(itemService.addItem(1, thirdItem))
                .thenReturn(thirdItem);

        mockMvc.perform(post("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(thirdItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is(thirdItem.getName())))
                .andExpect(jsonPath("$.description", is(thirdItem.getDescription())))
                .andExpect(jsonPath("$.available", is(thirdItem.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(1)));

        verify(itemService, times(1)).addItem(1, firstItem);
        verify(itemService, times(1)).addItem(1, secondItem);
        verify(itemService, times(1)).addItem(1, thirdItem);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should update item.")
    void shouldUpdateItem() throws Exception {
        when(itemService.addItem(1, firstItem))
                .thenReturn(firstItem);

        mockMvc.perform(post("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(firstItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstItem.getName())))
                .andExpect(jsonPath("$.description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.available", is(firstItem.getAvailable())));

        verify(itemService, times(1)).addItem(1, firstItem);

        firstItem.setName("UpdatedName");
        when(itemService.updateItem(1, firstItem))
                .thenReturn(firstItem);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(firstItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstItem.getName())))
                .andExpect(jsonPath("$.description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.available", is(firstItem.getAvailable())));

        verify(itemService, times(1)).updateItem(1, firstItem);
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get item by id.")
    void shouldGetItemById() throws Exception {
        when(itemService.addItem(1, firstItem))
                .thenReturn(firstItem);

        mockMvc.perform(post("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(firstItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(itemService.getItemDtoById(1, 1))
                .thenReturn(firstItem);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(firstItem.getName())))
                .andExpect(jsonPath("$.description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.available", is(firstItem.getAvailable())));
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should add new comment.")
    void shouldAddNewComment() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1)
                .text("comment")
                .authorName("author")
                .itemName("item")
                .build();

        when(itemService.addComment(1, 1, comment))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
                .andExpect(jsonPath("$.itemName", is(comment.getItemName())));

        verify(itemService, times(1)).addComment(1, 1, comment);
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should return users items.")
    void shouldReturnUsersItems() throws Exception {
        List<ItemDto> firstItemList = Collections.singletonList(firstItem);
        when(itemService.getItemsByUserId(1))
                .thenReturn(firstItemList);

        mockMvc.perform(get("/items")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is(firstItemList.get(0).getName())))
                .andExpect(jsonPath("$.[0].description", is(firstItemList.get(0).getDescription())))
                .andExpect(jsonPath("$.[0].available", is(firstItemList.get(0).getAvailable())));

        verify(itemService, times(1)).getItemsByUserId(1);
    }

    @Test
    @Order(value = 6)
    @DisplayName("6 - should search in items description.")
    void shouldSearchInItemsDescription() throws Exception {
        List<ItemDto> firstItemList = Collections.singletonList(firstItem);
        when(itemService.searchInDescription("rst"))
                .thenReturn(firstItemList);

        mockMvc.perform(get("/items/search")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .param("text", "rst")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is(firstItemList.get(0).getName())))
                .andExpect(jsonPath("$.[0].description", is(firstItemList.get(0).getDescription())))
                .andExpect(jsonPath("$.[0].available", is(firstItemList.get(0).getAvailable())));

        verify(itemService, times(1)).searchInDescription("rst");
    }
}
