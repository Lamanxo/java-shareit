package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemUserStorage;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final InMemUserStorage userStorage;
    private final InMemItemStorage itemStorage;

    public List<ItemDto> getAllItemsByUserId(long userId) {
        try {
            userStorage.getUser(userId).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", userId));
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemStorage.getItemsByUser(userId)) {
            itemDtoList.add(makeItemDto(item));
        }
        log.info("Getting users with id: {} items", userId);
        return itemDtoList;
    }

    public ItemDto createItem(ItemDto itemDto, long userId) {
        try {
            userStorage.getUser(userId).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", userId));
        }
        Item item = makeItem(itemDto);
        item.setOwner(userId);
        log.info("User {} create item", userId);
        return makeItemDto(itemStorage.addItem(item));
    }

    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        Item item = itemStorage.getItem(itemId).orElseThrow(() ->
                new ItemException(String.format("Item with id: %s not found", itemId)));
        if (item.getOwner() != userId) {
            log.warn("Item with id: {} not belong user with id {}", itemId, userId);
            throw new ItemException(
                    String.format("Item with id: %s does not belong to user with id: %s", itemId, userId));
        }
        itemStorage.deleteItem(itemId);
        if (itemDto.getName() != null) {
            log.info("Item with id: {} update name {}", itemId, itemDto.getName());
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            log.info("Item with id: {} update description {}", itemId, itemDto.getDescription());
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            log.info("Item with id: {} update available status {}", itemId, itemDto.getAvailable());
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Item with id: {}, owner with id: {} update", itemId, userId);
        return makeItemDto(itemStorage.addItem(item));
    }

    public List<ItemDto> searchItems(String word) {
        if (word.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemStorage.findItem(word)) {
            itemDtoList.add(makeItemDto(item));
        }
        log.info("Searching item with keyword {}", word);
        return itemDtoList;
    }

    public ItemDto getItemById(long itemId) {
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
