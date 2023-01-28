package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


import java.time.LocalDateTime;


@JsonTest
public class BookingDtoInTest {
    @Autowired
    private JacksonTester<BookingDtoIn> jacksonTester;

    @Test
    void testBookingDtoInJson() throws Exception {

        BookingDtoIn dtoIn = new BookingDtoIn(1L, LocalDateTime.now(), LocalDateTime.now());

        JsonContent<BookingDtoIn> jsonContent = jacksonTester.write(dtoIn);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isNotNull();
    }
}
