package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RequestService requestService;

    RequestDtoIn dtoIn = new RequestDtoIn("randomRequest");
    ItemDtoForRequest dto = new ItemDtoForRequest(1L, "randomItem", "RandomDesc", true, 33L);
    RequestDtoOut dtoOut = new RequestDtoOut(1L,"randomRequest", LocalDateTime.now(), List.of(dto));

    @Test
    void addRequestTest() throws Exception{
        when(requestService.addRequest(any(), any()))
                .thenReturn(dtoOut);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(dtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dtoOut.getDescription())))
                .andExpect(jsonPath("$.items.size()", is(dtoOut.getItems().size())));
    };

    @Test
    void getRequestsByUserTest() throws Exception {
        when(requestService.getRequestsByUser(any()))
                .thenReturn(List.of(dtoOut));

        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dtoOut.getDescription())))
                .andExpect(jsonPath("$[0].items.size()", is(dtoOut.getItems().size())));
    };

    @Test
    void getRequestsByIdTest() throws Exception {
        when(requestService.getRequestById(any(), any()))
                .thenReturn(dtoOut);

        mockMvc.perform(get("/requests/{requestId}", dtoOut.getId())
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dtoOut.getDescription())))
                .andExpect(jsonPath("$.items.size()", is(dtoOut.getItems().size())));
    };

    @Test
    void getAllByPageTest() throws Exception {
        when(requestService.getAllByPage(any(), any(), any()))
                .thenReturn(List.of(dtoOut));

        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dtoOut.getDescription())))
                .andExpect(jsonPath("$[0].items.size()", is(dtoOut.getItems().size())));
    };

}
