package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request makeRequest(RequestDtoIn dtoIn, Long requesterId) {
        Request request = new Request();
        request.setDescription(dtoIn.getDescription());
        request.setRequesterId(requesterId);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static RequestDtoOut makeRequestDtoOut(Request request) {
        RequestDtoOut dtoOut = new RequestDtoOut();
        dtoOut.setId(request.getId());
        dtoOut.setDescription(request.getDescription());
        dtoOut.setCreated(request.getCreated());
        return dtoOut;
    }
}
