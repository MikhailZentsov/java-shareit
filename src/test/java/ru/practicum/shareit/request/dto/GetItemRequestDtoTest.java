package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GetItemRequestDtoTest {
    @Autowired
    private JacksonTester<GetItemRequestDto> json;
    private static LocalDateTime time;

    @BeforeAll
    static void beforeAll() {
        time = LocalDateTime.now();
    }

    @Test
    void shouldSerialize() throws IOException {
        GetItemRequestDto dto = GetItemRequestDto.builder()
                .id(1L)
                .description("requestText")
                .created(time)
                .items(null)
                .build();

        JsonContent<GetItemRequestDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).hasJsonPathValue("$.created");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("requestText");
        assertThat(result).extractingJsonPathValue("$.created").isEqualTo(time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathValue("$.items").isEqualTo(null);
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"id\":\"1\",\"description\":\"requestText\",\"created\":\"" + time + "\"}";

        ObjectContent<GetItemRequestDto> result = json.parse(content);

        assertThat(result).isEqualTo(GetItemRequestDto.builder()
                .id(1L)
                .description("requestText")
                .created(time)
                .items(null)
                .build());
    }
}