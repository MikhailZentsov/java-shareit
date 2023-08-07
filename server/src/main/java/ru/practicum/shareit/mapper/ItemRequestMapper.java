package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequestFromCreateItemRequestDto(CreateItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();
    }

    public GetItemRequestDto toGetItemRequestDtoFromItemRequest(ItemRequest itemRequest) {

        return GetItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems() != null ?
                        itemRequest.getItems()
                                .stream()
                                .map(ItemMapper::toGetItemForGetItemRequestDtoFromItem)
                                .collect(Collectors.toList())
                        :
                        new ArrayList<>())
                .build();
    }
}
