package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public List<GetItemDto> getAllByUserId(long userId) {
        userStorage.getById(userId);

        return itemStorage.getAllByUserId(userId)
                .stream()
                .map(ItemMapper::getItemDtoFromItem)
                .collect(Collectors.toList());
    }

    @Override
    public GetItemDto getOneById(long userId, long itemId) {
        userStorage.getById(userId);

        return ItemMapper.getItemDtoFromItem(itemStorage.getOneById(itemId));
    }

    @Override
    public GetItemDto create(long userId, CreateUpdateItemDto createUpdateItemDto) {
        User user = userStorage.getById(userId);

        Item item = ItemMapper.getItemFromCreateUpdateItemDto(createUpdateItemDto);
        item.setOwner(user);

        return ItemMapper.getItemDtoFromItem(itemStorage.create(item));
    }

    @Override
    public GetItemDto update(long userId, long itemId, CreateUpdateItemDto updateItemDto) {
        User user = userStorage.getById(userId);

        Item item = itemStorage.getOneById(itemId);
        item.setOwner(user);

        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }

        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        return ItemMapper.getItemDtoFromItem(itemStorage.update(userId, item));
    }

    @Override
    public void delete(long userId, long itemId) {
        userStorage.getById(userId);

        itemStorage.delete(userId, itemId);
    }

    @Override
    public List<GetItemDto> search(long userId, String text) {
        userStorage.getById(userId);

        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemStorage.search(text)
                .stream()
                .map(ItemMapper::getItemDtoFromItem)
                .collect(Collectors.toList());
    }
}
