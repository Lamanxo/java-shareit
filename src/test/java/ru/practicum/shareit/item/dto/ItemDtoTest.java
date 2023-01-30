package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Test
    void testCommentDtoJson() throws IOException {
        Booking lastbooking = new Booking(1L, 22L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
        Booking nextbooking = new Booking(1L, 21L, 2L, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED);
        CommentDto commentDto = new CommentDto(1L, "randomText", "RandomAuthor", LocalDateTime.now());

        ItemDto dto = new ItemDto(1L, "randomItem", "RandomDesc", true, List.of(commentDto), lastbooking, nextbooking, 33L);

        JsonContent<ItemDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("randomItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("RandomDesc");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.lastBooking.itemId").isEqualTo(22);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.start").isNotBlank();
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.end").isNotBlank();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.nextBooking.itemId").isEqualTo(21);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking.start").isNotBlank();
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking.end").isNotBlank();
        assertThat(jsonContent).extractingJsonPathArrayValue("$.comments").isNotEmpty();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments.size()").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("randomText");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("RandomAuthor");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].created").isNotBlank();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(33);

    }
}
