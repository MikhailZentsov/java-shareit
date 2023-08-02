package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GetUserDtoTest {
    @Autowired
    private JacksonTester<GetUserDto> json;

    @Test
    void shouldSerialize() throws IOException {
        GetUserDto dto = GetUserDto.builder()
                .id(1L)
                .name("userName")
                .email("mail@ya.ru")
                .build();

        JsonContent<GetUserDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("mail@ya.ru");
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"id\":\"1\",\"name\":\"userName\",\"email\":\"mail@ya.ru\"}";

        ObjectContent<GetUserDto> result = json.parse(content);

        assertThat(result).isEqualTo(GetUserDto.builder()
                .id(1L)
                .name("userName")
                .email("mail@ya.ru")
                .build());
    }

}