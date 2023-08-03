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
class CreateUpdateUserDtoTest {
    @Autowired
    private JacksonTester<CreateUpdateUserDto> json;

    @Test
    void shouldSerialize() throws IOException {
        CreateUpdateUserDto dto = CreateUpdateUserDto.builder()
                .name("userName")
                .email("mail@ya.ru")
                .build();

        JsonContent<CreateUpdateUserDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("mail@ya.ru");
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"name\":\"userName\",\"email\":\"mail@ya.ru\"}";

        ObjectContent<CreateUpdateUserDto> result = json.parse(content);

        assertThat(result).isEqualTo(CreateUpdateUserDto.builder()
                .name("userName")
                .email("mail@ya.ru")
                .build());
    }
}