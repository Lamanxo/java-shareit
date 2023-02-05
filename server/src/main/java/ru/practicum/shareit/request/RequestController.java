package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService service;

    @PostMapping
    public RequestDtoOut addRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                    @RequestBody RequestDtoIn dtoIn) {
        return service.addRequest(requesterId, dtoIn);
    }

    @GetMapping
    public List<RequestDtoOut> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return service.getRequestsByUser(requesterId);
    }

    @GetMapping("/{requestId}")
    public RequestDtoOut getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long requestId) {
        return service.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<RequestDtoOut> getAllByPage(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "from", required = false) Integer from,
                                            @RequestParam(name = "size", required = false) Integer size) {
        return service.getAllByPage(userId, from, size);
    }
}
