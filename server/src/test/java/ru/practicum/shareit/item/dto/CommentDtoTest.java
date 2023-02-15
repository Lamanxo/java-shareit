package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    void testCommentDtoJson() throws IOException {
        CommentDto commentDto = new CommentDto(1L, "randomText", "RandomAuthor", LocalDateTime.now());

        JsonContent<CommentDto> jsonContent = jacksonTester.write(commentDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("randomText");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName").isEqualTo("RandomAuthor");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isNotNull();

    }
}
