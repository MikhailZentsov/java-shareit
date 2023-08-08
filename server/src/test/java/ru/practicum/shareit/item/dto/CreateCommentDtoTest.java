package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateCommentDtoTest {
    @Autowired
    private JacksonTester<CreateCommentDto> json;

    @Test
    void shouldSerialize() throws IOException {
        CreateCommentDto dto = CreateCommentDto.builder()
                .text("text")
                .build();

        JsonContent<CreateCommentDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"text\":\"commentText\"}";

        ObjectContent<CreateCommentDto> result = json.parse(content);

        assertThat(result).isEqualTo(CreateCommentDto.builder()
                .text("commentText")
                .build());
    }
}