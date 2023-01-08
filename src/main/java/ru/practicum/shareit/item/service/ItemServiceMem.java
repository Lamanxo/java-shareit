package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.repo.InMemItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repo.InMemUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("ItemServiceMem")
@RequiredArgsConstructor
@Slf4j
public class ItemServiceMem implements ItemService{
    private final InMemUserStorage userStorage;
    private final InMemItemStorage itemStorage;

    public List<ItemDto> getUserItems(long id) {
        try {
            userStorage.getUser(id).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", id));
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemStorage.getUserItems(id)) {
            itemDtoList.add(makeItemDto(item));
        }
        log.info("User's with id: {} items", id);
        return itemDtoList;
    }

    public ItemDto addItem(ItemDto itemDto, long id) {
        try {
            userStorage.getUser(id).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", id));
        }
        Item item = makeItem(itemDto);
        item.setOwner(id);
        log.info("User's {} item created", id);
        return makeItemDto(itemStorage.addItem(item));
    }

    public ItemDto updateItem(ItemDto itemDto, long itemId, long id) {
        Item item = itemStorage.getItem(itemId).orElseThrow(() ->
                new ItemException(String.format("Item with id: %s not found", itemId)));
        if (item.getOwner() != id) {
            log.warn("Item with id: {} not belongs to user with id {}", itemId, id);
            throw new ItemException(
                    String.format("Item with id: %s not belongs to user with id: %s", itemId, id));
        }
        itemStorage.deleteItem(itemId);
        if (itemDto.getName() != null) {
            log.info("Item with id: {} updated name {}", itemId, itemDto.getName());
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            log.info("Item with id: {} updated description {}", itemId, itemDto.getDescription());
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            log.info("Item with id: {} updated available status {}", itemId, itemDto.getAvailable());
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Item with id: {}, owner with id: {} updated", itemId, id);
        return makeItemDto(itemStorage.addItem(item));
    }

    public List<ItemDto> searchItem(String request) {
        if (request.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemStorage.searchItem(request)) {
            itemDtoList.add(makeItemDto(item));
        }
        log.info("Searching item {}", request);
        return itemDtoList;
    }

    @Override
    public CommentDto saveComment(CommentDto commentDto, Long userId, Long itemId) {
        return null;
    }

    public ItemDto getItem(long itemId, Long userId) {
        log.info("Getting item with id: {}", itemId);
        return makeItemDto(itemStorage.getItem(itemId).orElseThrow(() ->
                new ItemException(String.format("Item with id: %s not found", itemId))));
    }

    private ItemDto makeItemDto(Item item) {
        return ItemDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    private Item makeItem(ItemDto itemDto) {
        return Item.builder().id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

}
