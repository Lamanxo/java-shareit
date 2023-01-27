package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private ItemService itemService;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    BookingRepository bookingRepo;
    @Mock
    CommentRepository commentRepo;
    @Mock
    UserService userService;
    User user;
    Item item;
    ItemDto itemDto;
    CommentDto commentDto;
    Comment comment;
    Booking booking;

    @BeforeEach
    void init() {
        itemService = new ItemServiceDB(itemRepo, userService, commentRepo, bookingRepo);
        user = new User(1L, "randomUser", "random@mail.ru");
        item = new Item(1L, "randomItem", "randomDesc", true, 1L, 1L);
        itemDto = new ItemDto(1L, "itemDto", "itemDtoDesc", true, null, null,null, null);
        commentDto = new CommentDto(1L, "commentDesc", "randomAuthor", LocalDateTime.now());
        comment = new Comment(1L, "randomComment", 1L, 1L, LocalDateTime.now());
        booking = new Booking(1L, 1L, 1L, LocalDateTime.now().minusHours(5), LocalDateTime.now().minusHours(1), Status.APPROVED);
    }

    @Test
    void getItemTest() {
        ItemDto dto = ItemMapper.makeItemDto(item);
        when(itemRepo.findById(anyLong()))
                .thenReturn(Optional.of(item));
        ItemDto dto1 = itemService.getItem(1L, 1L);
        assertEquals(dto,dto1);
    }

    @Test
    void addItemTest() {
        ItemDto dto1 = ItemMapper.makeItemDto(item);
        when(itemRepo.save(any()))
                .thenReturn(ItemMapper.makeItem(dto1));
        ItemDto dto2 = itemService.addItem(itemDto, 1);
        assertEquals(dto1, dto2);
    }

    @Test
    void updateItemTest() {
        Item item1 = item;
        ItemDto updateItem = ItemDto.builder().name("update").build();
        item1.setOwner(1L);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepo.save(Mockito.any())).thenReturn(item1);
        assertEquals(updateItem.getName(), itemService.updateItem(updateItem, 1, 1).getName());
        verify(itemRepo).save(item1);
    }

    @Test
    void getUsersItemsTest() {


    }

    @Test
    void searchItemTest() {}



    @Test
    void addCommentTest() {
        when(itemRepo.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepo.findItemIdByBooker(anyLong(), anyLong()))
                .thenReturn(Optional.of(booking));
        when(commentRepo.save(any()))
                .thenReturn(commentDto);

        CommentDto dtoReturned = itemService.addComment(commentDto, 1L, 1L);
        assertEquals(commentDto, dtoReturned);
    }

}
