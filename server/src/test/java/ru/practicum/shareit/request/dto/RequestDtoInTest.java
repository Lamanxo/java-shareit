package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoInTest {

    @Autowired
    private JacksonTester<RequestDtoIn> jacksonTester;

    @Test
    void testRequestDtoInJson() throws Exception {

        RequestDtoIn dtoIn = new RequestDtoIn("randomDesc");

        JsonContent<RequestDtoIn> jsonContent = jacksonTester.write(dtoIn);

        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("randomDesc");
    }

}
