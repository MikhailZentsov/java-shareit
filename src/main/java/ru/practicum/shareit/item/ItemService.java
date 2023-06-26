package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetItemDto;

import java.util.List;

public interface ItemService {

    List<GetItemDto> getAllByUserId(long userId);

    GetItemDto getOneById(long userId, long itemId);

    GetItemDto create(long userId, CreateUpdateItemDto createUpdateItemDto);

    GetItemDto update(long userId, long itemId, CreateUpdateItemDto updateItemDto);

    void delete(long userId, long itemId);

    List<GetItemDto> search(long userId, String text);
}
