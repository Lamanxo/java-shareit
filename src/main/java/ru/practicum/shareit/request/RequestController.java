package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService service;

    @PostMapping
    public RequestDtoOut addRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                    @Valid @RequestBody RequestDtoIn dtoIn) {
        return service.addRequest(requesterId, dtoIn);
    }

    @GetMapping
    public List<RequestDtoOut> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return service.getRequestsByUser(requesterId);
    }

    @GetMapping("/{requestId}")
    public RequestDtoOut getRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                        @PathVariable Long requestId) {
        return service.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<RequestDtoOut> getAllByPage(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                    @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return service.getAllByPage(userId, from, size);
    }
}
