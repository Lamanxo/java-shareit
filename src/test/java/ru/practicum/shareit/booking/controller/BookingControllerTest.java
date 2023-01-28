package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookingService bookingService;

    UserDto userDto = new UserDto(1L, "Name", "name@email.com");

    Booking lastbooking = new Booking(1L, 22L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
    Booking nextbooking = new Booking(1L, 21L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
    CommentDto commentDto = new CommentDto(1L, "randomText", "RandomAuthor", LocalDateTime.now());

    ItemDto itemDto = new ItemDto(1L, "randomItem", "RandomDesc", true, List.of(commentDto), lastbooking, nextbooking, 33L);

    BookingDtoOut dtoOut = new BookingDtoOut(1L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED, itemDto, userDto);

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(any(), any()))
                .thenReturn(dtoOut);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(dtoOut))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(dtoOut.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(dtoOut.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(dtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(dtoOut.getItem().getName())));
    }

    @Test
    void approveTest() throws Exception {
        dtoOut.setStatus(Status.APPROVED);
        when(bookingService.approve(any(), any(), any()))
                .thenReturn(dtoOut);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(dtoOut.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(dtoOut.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(dtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(dtoOut.getItem().getName())));
    }

    @Test
    void getByIdTest() throws Exception {

        when(bookingService.getById(any(), any()))
                .thenReturn(dtoOut);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(dtoOut.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(dtoOut.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(dtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(dtoOut.getItem().getName())));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(bookingService.getAllByUser(any(), any(), any(), any()))
                .thenReturn(List.of(dtoOut));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end", is(notNullValue())))
                .andExpect(jsonPath("$[0].status", is(dtoOut.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(dtoOut.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(dtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(dtoOut.getItem().getName())));
    }

    @Test
    void getAllByOwnerTest() throws Exception {
        when(bookingService.getAllByOwner(any(), any(), any(), any()))
                .thenReturn(List.of(dtoOut));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end", is(notNullValue())))
                .andExpect(jsonPath("$[0].status", is(dtoOut.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(dtoOut.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(dtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(dtoOut.getItem().getName())));
    }

}
