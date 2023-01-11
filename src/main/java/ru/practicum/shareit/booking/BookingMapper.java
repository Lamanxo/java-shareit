package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking makeBooking(BookingDtoIn dtoIn) {
        Booking booking = new Booking();
        booking.setItemId(dtoIn.getItemId());
        booking.setStart(dtoIn.getStart());
        booking.setEnd(dtoIn.getEnd());
        return booking;
    }

    public static BookingDtoOut makeBookingDtoOut(Booking booking) {
        BookingDtoOut dto = new BookingDtoOut();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
