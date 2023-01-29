package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getUserItems(long id, Integer from, Integer size);

    ItemDto addItem(ItemDto itemDto, long id);

    ItemDto updateItem(ItemDto itemDto, long itemId, long id);

    List<ItemDto> searchItem(String request, Integer from, Integer size);

    ItemDto getItem(long itemId, Long userId);

    CommentDto addComment(CommentDto commentDto, Long userId, Long itemId);
}
