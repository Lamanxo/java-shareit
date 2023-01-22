package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;

import java.util.List;

public interface RequestService {

        RequestDtoOut addRequest(Long requesterId, RequestDtoIn dtoIn);

        List<RequestDtoOut> getRequestsByUser(Long userId);

        List<RequestDtoOut> getAllByPage(Long userId, Integer from, Integer size);

        RequestDtoOut getRequestById(Long userId, Long requestId);
}
