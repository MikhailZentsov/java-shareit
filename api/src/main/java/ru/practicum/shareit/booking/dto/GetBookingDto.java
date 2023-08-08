package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.GetBookingForItemDto;
import ru.practicum.shareit.user.dto.GetUserForGetBookingDto;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class GetBookingDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    private Status status;
    private GetUserForGetBookingDto booker;
    private GetBookingForItemDto item;
}
