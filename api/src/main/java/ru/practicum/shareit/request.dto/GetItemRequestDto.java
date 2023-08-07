package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.GetItemForGetItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class GetItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<GetItemForGetItemRequestDto> items;
}
