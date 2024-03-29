package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto) {
        return itemService.addItem(itemDto, userId);
    }

    @GetMapping("{id}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {

        return itemService.getItem(id, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text", defaultValue = "") String word,
                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                    @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return itemService.searchItem(word, from, size);
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                              @RequestBody ItemDto itemDto, @PathVariable Long id) {
        return itemService.updateItem(itemDto, id, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(commentDto, userId, itemId);
    }

}
