package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemItemStorage {

private final Map<Long, Item> items = new HashMap<>();
private Long itemId = 1L;

    public List<Item> getItemsByUser(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    public Optional<Item> getItem(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Item addItem(Item item) {
        if (items.containsKey(item.getId())) {
            throw new ItemException(String.format("Item with id %s already create", item.getId()));
        }
        if (item.getId() == 0) {
            item.setId(itemId++);
        }
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> findItem(String word) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(word.toLowerCase())
                        || item.getDescription().toLowerCase().contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Item deleteItem(Long id) {
        return items.remove(id);
    }

}
