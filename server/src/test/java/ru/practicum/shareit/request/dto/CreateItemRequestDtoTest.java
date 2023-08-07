package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateItemRequestDtoTest {
    @Autowired
    private JacksonTester<CreateItemRequestDto> json;

    @Test
    void shouldSerialize() throws IOException {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .description("requestText")
                .build();

        JsonContent<CreateItemRequestDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("requestText");
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"description\":\"requestText\"}";

        ObjectContent<CreateItemRequestDto> result = json.parse(content);

        assertThat(result).isEqualTo(CreateItemRequestDto.builder()
                .description("requestText")
                .build());
    }
}