package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Min;
import java.util.List;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDtoOut addBooking(@RequestHeader ("X-Sharer-User-Id") Long userId,
                                     @RequestBody BookingDtoIn dtoIn) {
        return bookingService.addBooking(userId, dtoIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId) {
        return bookingService.getById(userId,bookingId);
    }

    @GetMapping()
    public List<BookingDtoOut> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false, defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }

}
