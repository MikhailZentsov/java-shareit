package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.GetItemBookingDto;

import java.util.SortedSet;

@Data
@Builder
public class GetItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private GetItemBookingDto lastBooking;
    private GetItemBookingDto nextBooking;
    private SortedSet<GetCommentDto> comments;
}
