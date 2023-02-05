package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


import java.time.LocalDateTime;
import java.util.List;

@JsonTest
public class BookingDtoOutTest {

    @Autowired
    private JacksonTester<BookingDtoOut> jacksonTester;

    @Test
    void testBookingDtoOut() throws Exception {

        UserDto userDto = new UserDto(1L, "Name", "name@email.com");

        Booking lastbooking = new Booking(1L, 22L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
        Booking nextbooking = new Booking(1L, 21L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
        CommentDto commentDto = new CommentDto(1L, "randomText", "RandomAuthor", LocalDateTime.now());

        ItemDto itemDto = new ItemDto(1L, "randomItem", "RandomDesc", true, List.of(commentDto), lastbooking, nextbooking, 33L);

        BookingDtoOut dtoOut = new BookingDtoOut(1L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED, itemDto, userDto);

        JsonContent<BookingDtoOut> jsonContent = jacksonTester.write(dtoOut);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo("randomItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.description").isEqualTo("RandomDesc");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathArrayValue("$.item.comments").isNotEmpty();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.comments.size()").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.comments.[0].id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.comments.[0].text").isEqualTo("randomText");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.comments.[0].authorName").isEqualTo("RandomAuthor");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.comments.[0].created").isNotBlank();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.lastBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.lastBooking.itemId").isEqualTo(22);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.lastBooking.bookerId").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.lastBooking.start").isNotBlank();
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.lastBooking.end").isNotBlank();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.nextBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.nextBooking.itemId").isEqualTo(21);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.nextBooking.bookerId").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.nextBooking.start").isNotBlank();
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.nextBooking.end").isNotBlank();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(33);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("Name");
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email").isEqualTo("name@email.com");
    }
}
