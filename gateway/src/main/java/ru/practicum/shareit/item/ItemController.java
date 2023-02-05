package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Adding item {} by user {}", itemDto.getName(), userId);
        return itemClient.addItem(itemDto, userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long id) {
        log.info("Getting item {} by user {}", id, userId);
        return itemClient.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        log.info("Getting all items of user {} with pagination from {} and size {}", userId, from, size);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(name = "text", defaultValue = "") String word,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        log.info("Searching item with keyword {} and pagination from {}, size {}", word, from, size);
        return itemClient.searchItem(word, from, size);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                              @RequestBody ItemDto itemDto, @PathVariable Long id) {
        log.info("Updating item with id {} and desc {} by user {}", id, itemDto.getDescription(), userId);
        return itemClient.updateItem(itemDto, id, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Adding comment {} to item {} by user {}", commentDto.getText(), itemId, userId);
        return itemClient.addComment(commentDto, userId, itemId);
    }
}
