package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookingController")
@WebMvcTest(controllers = BookingController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class BookingControllerTests {
    private final String HTTP_HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {
        bookingDto = BookingDto.builder()
                .id(1)
                .booker(UserDto.builder()
                        .id(4)
                        .name("FirstUser")
                        .email("FirstUser@somemail.com")
                        .build())
                .item(ItemDto.builder()
                        .id(1)
                        .name("FirstItemName")
                        .description("FirstItemDescription")
                        .available(true)
                        .build())
                .start(LocalDateTime.of(2024, 1, 2, 18, 15, 30))
                .end(LocalDateTime.of(2024, 1, 10, 11, 50, 30))
                .status(Status.WAITING.toString())
                .build();
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should add new booking.")
    void shouldAddNewBooking() throws Exception {
        when(bookingService.createBooking(1, bookingDto))
                .thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .header(HTTP_HEADER_USER_ID, 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));
        verify(bookingService, times(1)).createBooking(1, bookingDto);
    }

    @Test
    @Order(value = 2)
    @DisplayName("2 - should change item status.")
    void shouldChangeStatus() throws Exception {
        when(bookingService.setStatus(1, 1, true))
                .thenReturn(bookingDto);
        bookingDto.setStatus(Status.APPROVED.toString());
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
        verify(bookingService, times(1)).setStatus(1, 1, true);


        when(bookingService.setStatus(1, 1, false))
                .thenReturn(bookingDto);
        bookingDto.setStatus(Status.REJECTED.toString());
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .param("approved", "false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.REJECTED.toString())));
        verify(bookingService, times(1)).setStatus(1, 1, false);
    }

    @Test
    @Order(value = 3)
    @DisplayName("3 - should get info about booking by id.")
    void shouldGetInfoAboutBookingById() throws Exception {
        when(bookingService.getBookingInfo(1, 1))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header(HTTP_HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));

        verify(bookingService, times(1)).getBookingInfo(1, 1);
    }

    @Test
    @Order(value = 4)
    @DisplayName("4 - should get users bookings.")
    void shouldGetUsersBookings() throws Exception {
        when(bookingService.getUsersBookings(4, "ALL"))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(HTTP_HEADER_USER_ID, 4)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].booker.id", is(4)))
                .andExpect(jsonPath("$.[0].item.id", is(1)))
                .andExpect(jsonPath("$.[0].status", is(Status.WAITING.toString())));

        verify(bookingService, times(1)).getUsersBookings(4, "ALL");
    }

    @Test
    @Order(value = 5)
    @DisplayName("5 - should get users items bookings.")
    void shouldGetUsersItemsBookings() throws Exception {
        when(bookingService.getUsersItemsBookings(4, "ALL"))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(HTTP_HEADER_USER_ID, 4)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].booker.id", is(4)))
                .andExpect(jsonPath("$.[0].item.id", is(1)))
                .andExpect(jsonPath("$.[0].status", is(Status.WAITING.toString())));

        verify(bookingService, times(1)).getUsersItemsBookings(4, "ALL");
    }
}
