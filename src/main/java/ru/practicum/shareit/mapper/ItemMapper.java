package ru.practicum.shareit.mapper;

import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static GetItemDto getItemDtoFromItem(Item item) {
        return GetItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item getItemFromCreateUpdateItemDto(CreateUpdateItemDto createUpdateItemDto) {
        return Item.builder()
                .name(createUpdateItemDto.getName())
                .description(createUpdateItemDto.getDescription())
                .available(createUpdateItemDto.getAvailable())
                .build();
    }
}
