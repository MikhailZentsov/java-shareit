package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.MethodArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.SORT_BY_ID_ASC;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestStorage requestStorage;

    @Transactional(readOnly = true)
    @Override
    public List<GetItemDto> getAllByUserId(long userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, SORT_BY_ID_ASC);
        List<Item> items = itemStorage.findAllByOwnerId(userId, pageable).getContent();

        if (!items.isEmpty() && items.get(0).getOwner().getId() == userId) {
            return items.stream()
                    .map(ItemMapper::toGetItemWIthBookingDtoFromItem)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GetItemDto getOneById(long userId, long itemId) {
        userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (item.getOwner().getId() == userId) {
            return ItemMapper.toGetItemWIthBookingDtoFromItem(item);
        } else {
            return ItemMapper.toGetItemDtoFromItem(item);
        }
    }

    @Override
    public GetItemDto create(long userId, CreateUpdateItemDto createUpdateItemDto) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = ItemMapper.toItemFromCreateUpdateItemDto(createUpdateItemDto);
        item.setOwner(user);

        if (createUpdateItemDto.getRequestId() != null) {
            ItemRequest request = requestStorage.findById(createUpdateItemDto.getRequestId()).orElseThrow(
                    () -> new NotFoundException("Запрос на вещь не найден")
            );
            item.setRequest(request);
        }

        return ItemMapper.toGetItemDtoFromItem(itemStorage.save(item));
    }

    @Override
    public GetItemDto update(long userId, long itemId, CreateUpdateItemDto updateItemDto) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (!item.getOwner().equals(user)) {
            throw new NotFoundException(
                    String.format("У пользователя с ID = %s не найдена вещь с ID = %s", user.getId(), item.getId())
            );
        }

        if (updateItemDto.getName() != null && !updateItemDto.getName().isBlank()) {
            item.setName(updateItemDto.getName());
        }

        if (updateItemDto.getDescription() != null && !updateItemDto.getDescription().isBlank()) {
            item.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        return ItemMapper.toGetItemDtoFromItem(itemStorage.save(item));
    }

    @Override
    public void delete(long userId, long itemId) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (!item.getOwner().equals(user)) {
            throw new NotFoundException(
                    String.format("У пользователя с ID = %s не найдена вещь с ID = %s", user.getId(), item.getId())
            );
        }

        itemStorage.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetItemDto> search(long userId, String text, int from, int size) {
        userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Pageable pageable = new OffsetBasedPageRequest(from, size, SORT_BY_ID_ASC);

        return itemStorage.search(text, pageable)
                .getContent()
                .stream()
                .map(ItemMapper::toGetItemDtoFromItem)
                .collect(Collectors.toList());
    }

    @Override
    public GetCommentDto createComment(long userId, long itemId, CreateCommentDto commentDto) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (isBookingByUser(user, item)) {
            Comment comment = CommentMapper.toCommentFromCreateCommentDto(commentDto);

            comment.setAuthor(user);
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setCreated(LocalDateTime.now());

            return CommentMapper.toGetCommentDtoFromComment(commentStorage.save(comment));
        } else {
            throw new MethodArgumentException(String.format(
                    "Пользователь с ID = %s не брал в аренду вещь с ID = %s", userId, itemId));
        }
    }

    private @NotNull Boolean isBookingByUser(User user, @NotNull Item item) {
        LocalDateTime currentTime = LocalDateTime.now();
        return item.getBookings() != null && item.getBookings().stream()
                .anyMatch(t -> t.getBooker().equals(user)
                        && t.getEndDate().isBefore(currentTime)
                && t.getStatus().equals(Status.APPROVED));
    }
}
