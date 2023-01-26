package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceDB itemService;

    @Mock
    private ItemRepository itemRepo;

    @Mock
    UserService userService;

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("randomItem")
            .description("randomDesc")
            .available(true)
            .comments(new ArrayList<>())
            .build();

    private final Item item = ItemMapper.makeItem(itemDto);

    @Test
    void addItem() {


        when(itemRepo.save(any()))
                .thenReturn(ItemMapper.makeItem(itemDto));
        ItemDto dto1 = itemService.addItem(itemDto, 1);
        assertEquals(itemDto, dto1);
    }

    @Test
    void updateItem() {
        Item item1 = ItemMapper.makeItem(itemDto);
        item1.setOwner(1L);
        when(itemRepo.findById(anyLong()))
                .thenReturn(Optional.of(item1));

        ItemDto itemDto1 = itemService.updateItem(itemDto, 1, 1);
        assertEquals(itemDto1, itemDto);
    }

}
