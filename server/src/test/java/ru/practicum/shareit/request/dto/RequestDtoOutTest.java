package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


import java.time.LocalDateTime;
import java.util.List;

@JsonTest
public class RequestDtoOutTest {

    @Autowired
    private JacksonTester<RequestDtoOut> jacksonTester;

    @Test
    void testRequestDtoOutJson() throws Exception {

        ItemDtoForRequest dto = new ItemDtoForRequest(1L, "randomItem", "RandomDesc", true, 33L);
        RequestDtoOut dtoOut = new RequestDtoOut(1L,"randomRequest", LocalDateTime.now(), List.of(dto));

        JsonContent<RequestDtoOut> jsonContent = jacksonTester.write(dtoOut);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("randomRequest");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isNotNull();
        assertThat(jsonContent).extractingJsonPathArrayValue("$.items").isNotEmpty();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.size()").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("randomItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.items.[0].description").isEqualTo("RandomDesc");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(33);

    }
}
