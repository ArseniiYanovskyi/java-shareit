package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookingController")
@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTests {
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    private ItemRequestService requestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("test description.")
                .created(LocalDateTime.of(2023, 9, 22, 14, 32, 16))
                .items(Collections.singletonList(
                        ItemDto.builder()
                                .id(1)
                                .name("FirstItemName")
                                .description("FirstItemDescription")
                                .available(true)
                                .build()
                ))
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - add new request.")
    void shouldAddNewRequest() throws Exception {
        when(requestService.addNewRequest(1, itemRequestDto))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items.[0].id", is(1)));

        verify(requestService, times(1)).addNewRequest(1, itemRequestDto);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - get user requests.")
    void shouldGetUserRequests() throws Exception {
        when(requestService.getUserRequests(1))
                .thenReturn(Collections.singletonList(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].items.[0].id", is(1)));

        verify(requestService, times(1)).getUserRequests(1);
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - get other users requests.")
    void shouldGetOtherUsersRequests() throws Exception {
        when(requestService.getOtherUsersRequests(2))
                .thenReturn(Collections.singletonList(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header(HTTP_HEADER_USER_ID, 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].items.[0].id", is(1)));

        verify(requestService, times(1)).getOtherUsersRequests(2);
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - get request by id.")
    void shouldGetRequestById() throws Exception {
        when(requestService.getRequest(1, 1))
                .thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items.[0].id", is(1)));

        verify(requestService, times(1)).getRequest(1, 1);
    }
}
