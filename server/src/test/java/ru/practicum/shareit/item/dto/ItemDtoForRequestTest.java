package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoForRequestTest {

    @Autowired
    private JacksonTester<ItemDtoForRequest> jacksonTester;

    @Test
    void testCommentDtoJson() throws IOException {
        ItemDtoForRequest dto = new ItemDtoForRequest(1L, "randomItem", "RandomDesc", true, 33L);

        JsonContent<ItemDtoForRequest> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("randomItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("RandomDesc");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(33);

    }
}
