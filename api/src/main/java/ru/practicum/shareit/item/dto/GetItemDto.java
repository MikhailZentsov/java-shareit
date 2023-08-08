package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.GetBookingForGetItemDto;
import ru.practicum.shareit.item.dto.GetCommentDto;

import java.util.SortedSet;

@Data
@Builder(toBuilder = true)
public class GetItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private GetBookingForGetItemDto lastBooking;
    private GetBookingForGetItemDto nextBooking;
    private SortedSet<GetCommentDto> comments;
}
