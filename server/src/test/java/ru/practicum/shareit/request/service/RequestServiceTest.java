package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repo.RequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTest {

    @InjectMocks
    RequestServiceDb service;
    @Mock
    RequestRepository requestRepo;
    @Mock
    UserService userService;
    @Mock
    ItemRepository itemRepo;

    final User user = new User(1L, "randomUser", "random@mail.ru");
    final RequestDtoIn dtoIn = new RequestDtoIn("randomDesc");
    final Request request = new Request(1L, "randomDesc", 2L, LocalDateTime.now());
    final ItemDtoForRequest itemDtoReq = new ItemDtoForRequest(1L, "randomItem", "RandomDesc", true, 33L);


    @Test
    void getRequestByIdTest() {
        when(requestRepo.findById(anyLong()))
                .thenReturn(Optional.of(request));
        RequestDtoOut dtoOutReturned = service.getRequestById(1L, 1L);
        assertEquals(1,dtoOutReturned.getId());

    }

     @Test
    void getAllByPageTest() {

         Page<Request> requests = new PageImpl<>(List.of(request));

         when(requestRepo.findAll(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "created"))))
                 .thenReturn(requests);

         List<RequestDtoOut> dtoOutsReturned = service.getAllByPage(user.getId(), 0, 20);

         assertThat(dtoOutsReturned.size()).isEqualTo(1);
         assertThat(dtoOutsReturned.get(0).getId()).isEqualTo(1L);
         assertThat(dtoOutsReturned.get(0).getDescription()).isEqualTo("randomDesc");
         assertThat(dtoOutsReturned.get(0).getCreated()).isNotNull();
         assertThat(dtoOutsReturned.get(0).getItems()).isEqualTo(List.of());
    }

    @Test
    void getRequestsByUserTest() {

        when(requestRepo.findAllByRequesterId(anyLong()))
                .thenReturn(List.of(request));
        when(itemRepo.findAllByRequestId(anyLong()))
                .thenReturn(List.of(itemDtoReq));
        List<RequestDtoOut> dtoOutsReturned = service.getRequestsByUser(1L);
        assertEquals(1, dtoOutsReturned.size());
    }

    @Test
    void addRequestTest() {

        when(requestRepo.save(any()))
                .thenReturn(request);
        when(itemRepo.findAllByRequestId(anyLong()))
                .thenReturn(List.of(itemDtoReq));
        RequestDtoOut dtoOutReturned = service.addRequest(1L, dtoIn);
        assertEquals(1L, dtoOutReturned.getId());

    }

}
