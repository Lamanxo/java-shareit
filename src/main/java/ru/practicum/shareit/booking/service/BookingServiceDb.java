package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceDb implements BookingService {

    BookingRepository bookingRepo;
    UserService userService;
    ItemService itemService;


    public BookingServiceDb(BookingRepository bookingRepo, @Qualifier("UserServiceDB") UserService userService,
                            @Qualifier("ItemServiceDB") ItemService itemService) {
        this.bookingRepo = bookingRepo;
        this.userService = userService;
        this.itemService = itemService;

    }

    @Override
    public BookingDtoOut addBooking(Long userId, BookingDtoIn dtoIn) {
        userService.getUser(userId);
        ItemDto itemDto = itemService.getItem(dtoIn.getItemId(),userId);
        Booking booking = makeBooking(dtoIn);
        List<ItemDto> itemDtos = itemService.getUserItems(userId);
        if (itemDtos.contains(itemDto)) {
            throw new BookingException("Owner cant booking his own item");
        }
        if (!itemService.getItem(booking.getItemId(), userId).getAvailable() || dtoIn.getStart().isAfter(dtoIn.getEnd()) ||
        dtoIn.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Item is not available");
        }
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);
        return makeDtoOut(bookingRepo.save(booking), userId);
    }

    @Override
    public BookingDtoOut approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingOrException(bookingId);
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException("Booking already approved");
        }
        if (approved && ownerCheck(userId, bookingId)) {
             booking.setStatus(Status.APPROVED);
        } else if (!approved && ownerCheck(userId,bookingId)) {
             booking.setStatus(Status.REJECTED);
        } else {
            throw new BookingException("Only owner approves booking");
        }
        return makeDtoOut(bookingRepo.save(booking), userId);
    }

    @Override
    public BookingDtoOut getById(Long userId, Long bookingId) {
        Booking booking = bookingOrException(bookingId);
        if (!booking.getBookerId().equals(userId) && !ownerCheck(userId,bookingId)) {
            throw new BookingException("User: " + userId + " not owns booking with id: " + bookingId);
        }
        return makeDtoOut(booking, userId);
    }

    @Override
    public List<BookingDtoOut> getAllByUser(Long userId, String state) {
        if (!ObjectUtils.containsConstant(State.values(), state)) {
            throw new BadRequestException("Unknown state: " + state);
        }
        userService.getUser(userId);
        List<Booking> bookings = bookingRepo.findAllByBookerIdOrderByStartDesc(userId);
        bookings = getBookingsByState(State.valueOf(state), bookings);
        List<BookingDtoOut> dtoOuts = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoOuts.add(makeDtoOut(booking,userId));
        }
        return dtoOuts;
    }

    @Override
    public List<BookingDtoOut> getAllByOwner(Long userId, String state) {
        if (!ObjectUtils.containsConstant(State.values(), state)) {
            throw new BadRequestException("Unknown state: " + state);
        }
        userService.getUser(userId);
        List<Booking> bookings = bookingRepo.findAllByOwnerStartDesc(userId);
        bookings = getBookingsByState(State.valueOf(state),bookings);
        List<BookingDtoOut> dtoOuts = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoOuts.add(makeDtoOut(booking, userId));
        }
        return dtoOuts;
    }

    private List<Booking> getBookingsByState(State state, List<Booking> bookings) {
        switch (state) {
            case ALL:
                return bookings;
            case WAITING:
            case REJECTED:
                Status status = Status.valueOf(state.toString());
                return bookings.stream()
                        .filter(booking -> booking.getStatus().equals(status))
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getEnd()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(booking -> LocalDateTime.now().isBefore(booking.getStart()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookings.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getStart())
                                && LocalDateTime.now().isBefore(booking.getEnd()))
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Invalid state: " + state);
        }
    }

    private Booking bookingOrException(Long bookingId) {
        return bookingRepo.findById(bookingId).orElseThrow(() ->
                new BookingException("Booking with id: " + bookingId + " not found"));
    }

    private Boolean ownerCheck(Long userId, Long bookingId) {
        List<ItemDto> listDto = itemService.getUserItems(userId);
        Booking booking = bookingOrException(bookingId);
        if (!listDto.contains(itemService.getItem(booking.getItemId(), userId))) {
            return false;
        }
        return true;
    }

    private Booking makeBooking(BookingDtoIn dtoIn) {
        Booking booking = new Booking();
        booking.setItemId(dtoIn.getItemId());
        booking.setStart(dtoIn.getStart());
        booking.setEnd(dtoIn.getEnd());
        return booking;
    }

    private BookingDtoOut makeDtoOut(Booking booking, Long userId) {
        BookingDtoOut dto = new BookingDtoOut();
        dto.setId(booking.getId());
        dto.setItem(itemService.getItem(booking.getItemId(), userId));
        dto.setBooker(userService.getUser(booking.getBookerId()));
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
