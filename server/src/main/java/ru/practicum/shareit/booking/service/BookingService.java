package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut addBooking(Long userId, BookingDtoIn bookingDtoIn);

    BookingDtoOut approve(Long userId, Long bookingId, Boolean approved);

    BookingDtoOut getById(Long userId, Long bookingId);

    List<BookingDtoOut> getAllByUser(Long userId, String state, Integer from, Integer size);

    List<BookingDtoOut> getAllByOwner(Long userId, String state, Integer from, Integer size);
}
