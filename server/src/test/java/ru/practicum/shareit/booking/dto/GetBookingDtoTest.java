package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GetBookingDtoTest {
    @Autowired
    private JacksonTester<GetBookingDto> json;
    private static LocalDateTime startTime;
    private static LocalDateTime endTime;

    @BeforeAll
    static void beforeAll() {
        startTime = LocalDateTime.now().minusDays(2);
        endTime = LocalDateTime.now().minusDays(1);
    }

    @Test
    void shouldSerialize() throws IOException {
        GetBookingDto dto = GetBookingDto.builder()
                .id(1L)
                .start(startTime)
                .end(endTime)
                .status(Status.APPROVED)
                .build();

        JsonContent<GetBookingDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
        assertThat(result).hasJsonPathValue("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo(Status.APPROVED.toString());
    }

    @Test
    void shouldDeserialize() throws IOException {
        String content = "{\"id\":\"1\",\"start\":\"" + startTime + "\",\"end\":\"" + endTime + "\",\"status\":\"" + Status.APPROVED + "\"}";

        ObjectContent<GetBookingDto> result = json.parse(content);

        assertThat(result).isEqualTo(GetBookingDto.builder()
                .id(1L)
                .start(startTime)
                .end(endTime)
                .status(Status.APPROVED)
                .build());
    }
}