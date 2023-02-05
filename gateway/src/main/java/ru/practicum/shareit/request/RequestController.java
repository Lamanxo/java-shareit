package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                             @Valid @RequestBody RequestDto dtoIn) {
        log.info("Adding request {} by user {}", dtoIn.getDescription(), requesterId);
        return requestClient.addRequest(requesterId, dtoIn);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        log.info("Getting own requests by user {}", requesterId);
        return requestClient.getRequestsByUser(requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Getting request {} by user {}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllByPage(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Getting user {} requests with pagination from {} and size {}", userId, from, size);
        return requestClient.getAllByPage(userId, from, size);
    }
}
