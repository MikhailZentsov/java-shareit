package ru.practicum.shareit.booking.dto;

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
class CreateBookingDtoTest {
    @Autowired
    private JacksonTester<CreateBookingDto> json;
    private static LocalDateTime startTime;
    private static LocalDateTime endTime;

    @BeforeAll
    static void beforeAll() {
        startTime = LocalDateTime.now().minusDays(2);
        endTime = LocalDateTime.now().minusDays(1);
    }

    @Test
    void shouldSerialize() throws IOException {
        CreateBookingDto dto = CreateBookingDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build();

        JsonContent<CreateBookingDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.itemId");
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"itemId\":\"1\",\"start\":\"" + startTime + "\",\"end\":\"" + endTime + "\"}";

        ObjectContent<CreateBookingDto> result = json.parse(content);

        assertThat(result).isEqualTo(CreateBookingDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build());
    }
}