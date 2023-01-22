package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repo.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceDb implements RequestService {

    private final ItemRepository itemRepo;
    private final RequestRepository requestRepo;
    private final UserService userService;


    @Override
    public RequestDtoOut addRequest(Long requesterId, RequestDtoIn dtoIn) {
        userService.getUser(requesterId);
        Request request = RequestMapper.makeRequest(dtoIn,requesterId);
        return makeFullDtoOut(requestRepo.save(request));
    }

    @Override
    public List<RequestDtoOut> getRequestsByUser(Long userId) {
        userService.getUser(userId);
        List<Request> requests = new ArrayList<>(requestRepo.findAllByRequesterId(userId));
        List<RequestDtoOut> dtoOuts = new ArrayList<>();
        for (Request request : requests) {
            dtoOuts.add(makeFullDtoOut(request));
        }
        return dtoOuts;
    }

    @Override
    public List<RequestDtoOut> getAllByPage(Long userId, Integer from, Integer size) {
        userService.getUser(userId);
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");

        Pageable page = PageRequest.of(from / size, size, sortByCreated);

        List<Request> requests = requestRepo.findAll(page).stream()
                .filter(request -> !request.getRequesterId().equals(userId))
                .collect(Collectors.toList());

        List<RequestDtoOut> dtoOuts = new ArrayList<>();
        for (Request request : requests) {
            dtoOuts.add(makeFullDtoOut(request));
        }

        return dtoOuts;
    }

    @Override
    public RequestDtoOut getRequestById(Long userId, Long requestId) {
        userService.getUser(userId);
        return makeFullDtoOut(requestOrException(requestId));
    }

    private RequestDtoOut makeFullDtoOut(Request request) {
        RequestDtoOut dtoOut = RequestMapper.makeRequestDtoOut(request);
        List<ItemDtoForRequest> listItems = new ArrayList<>(itemRepo.findAllByRequestId(request.getId()));
        dtoOut.setItems(listItems);
        return dtoOut;
    }

    private Request requestOrException(Long requestId) {
        return requestRepo.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException("Request with id: " + requestId + " not found"));
    }
}
