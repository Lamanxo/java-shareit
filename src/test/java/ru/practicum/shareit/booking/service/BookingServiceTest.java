package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceTest {

    @InjectMocks
    BookingServiceDb service;
    @Mock
    UserService userService;
    @Mock
    ItemService itemService;
    @Mock
    BookingRepository bookingRepo;


    final User user = new User(1L, "randomUser", "random@mail.ru");
    final Item item = new Item(1L, "randomItem", "randomDesc", true, 1L, 1L);
    final ItemDto itemDto = new ItemDto(1L, "itemDto", "itemDtoDesc", true, null, null,null, null);
    final BookingDtoIn dtoIn = new BookingDtoIn(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(15));
    final Booking booking = new Booking(1L, 1L,1L,dtoIn.getStart(), dtoIn.getEnd(), Status.APPROVED);

    @Test
    void addBookingTest() {
        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.makeUserDto(user));
        when(itemService.getItem(anyLong(),anyLong()))
                .thenReturn(itemDto);
        when(itemService.getUserItems(anyLong(),any(),any()))
                .thenReturn(List.of());
        when(bookingRepo.save(any()))
                .thenReturn(booking);
        BookingDtoOut bookingDtoOutReturned = service.addBooking(1L, dtoIn);
        assertEquals(Status.APPROVED, bookingDtoOutReturned.getStatus());
        verify(bookingRepo).save(any());
    }

    @Test
    void approveRejectedTest() {
        booking.setStatus(Status.WAITING);
        when(bookingRepo.save(any()))
                .thenReturn(booking);
        when(bookingRepo.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(itemService.getUserItems(anyLong(),any(),any()))
                .thenReturn(List.of(itemDto));
        when(itemService.getItem(anyLong(),anyLong()))
                .thenReturn(itemDto);
        BookingDtoOut dtoOutReturned = service.approve(1L,1L, false);
        assertEquals(Status.REJECTED, dtoOutReturned.getStatus());

    }

    @Test
    void approveApprovedTest() {
        booking.setStatus(Status.WAITING);
        when(bookingRepo.save(any()))
                .thenReturn(booking);
        when(bookingRepo.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(itemService.getUserItems(anyLong(),any(),any()))
                .thenReturn(List.of(itemDto));
        when(itemService.getItem(anyLong(),anyLong()))
                .thenReturn(itemDto);
        BookingDtoOut dtoOutReturned = service.approve(1L,1L, true);
        assertEquals(Status.APPROVED, dtoOutReturned.getStatus());

    }

    @Test
    void getByIdTest() {
        when(bookingRepo.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        BookingDtoOut dtoOutReturned = service.getById(1L,1L);
        assertEquals(1L, dtoOutReturned.getId());
    }

    @Test
    void getByIdWrongUserIdTest() {
        when(bookingRepo.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        assertThrows(BookingException.class, () -> service.getById(2L,1L));
    }

    @Test
    void getAllByUserStateFutureTest() {
        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.makeUserDto(user));
        when(bookingRepo.findAllByBookerIdOrderByStartDesc(anyLong(),any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        List<BookingDtoOut> dtoOutsReturned = service.getAllByUser(1L, "FUTURE",1,20);
        assertEquals(1, dtoOutsReturned.size());
    }

    @Test
    void getAllByUserStateAllTest() {
        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.makeUserDto(user));
        when(bookingRepo.findAllByBookerIdOrderByStartDesc(anyLong(),any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        List<BookingDtoOut> dtoOutsReturned = service.getAllByUser(1L, "ALL",1,20);
        assertEquals(1, dtoOutsReturned.size());
    }

    @Test
    void getAllByStateAllOwner() {
        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.makeUserDto(user));
        when(bookingRepo.findAllByOwnerStartDesc(anyLong(),any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        List<BookingDtoOut> dtoOutsReturned = service.getAllByOwner(1L,"ALL",1,20);
        assertEquals(1,dtoOutsReturned.size());
    }

    @Test
    void getAllByStateFutureOwner() {
        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.makeUserDto(user));
        when(bookingRepo.findAllByOwnerStartDesc(anyLong(),any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        List<BookingDtoOut> dtoOutsReturned = service.getAllByOwner(1L,"FUTURE",1,20);
        assertEquals(1,dtoOutsReturned.size());
    }
}
