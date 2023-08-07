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
class CreateUpdateItemDtoTest {
    @Autowired
    private JacksonTester<CreateUpdateItemDto> json;

    @Test
    void shouldSerialize() throws IOException {
        CreateUpdateItemDto dto = CreateUpdateItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(4L)
                .build();

        JsonContent<CreateUpdateItemDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).hasJsonPathNumberValue("$.requestId");

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(4);
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"name\":\"itemName\",\"description\":\"itemDescription\",\"available\":\"true\",\"requestId\":\"4\"}";

        ObjectContent<CreateUpdateItemDto> result = json.parse(content);

        assertThat(result).isEqualTo(CreateUpdateItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(4L)
                .build());
    }
}