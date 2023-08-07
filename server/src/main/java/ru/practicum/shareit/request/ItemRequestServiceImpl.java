package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.SORT_BY_CREATED_DESC;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    @Override
    public GetItemRequestDto createRequest(long userId, CreateItemRequestDto itemRequestDto) {
        User requester = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        ItemRequest request = ItemRequestMapper.toItemRequestFromCreateItemRequestDto(itemRequestDto);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toGetItemRequestDtoFromItemRequest(itemRequestStorage.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetItemRequestDto> getAllRequestsByUserId(long userId) {
        User requester = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        return itemRequestStorage.getAllByRequester(requester, SORT_BY_CREATED_DESC)
                .stream()
                .map(ItemRequestMapper::toGetItemRequestDtoFromItemRequest)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetItemRequestDto> getAllRequests(long userId, int from, int size) {
        User requester = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Pageable pageable = new OffsetBasedPageRequest(from, size, SORT_BY_CREATED_DESC);

        return itemRequestStorage.getAllByRequesterNot(requester, pageable)
                .stream()
                .map(ItemRequestMapper::toGetItemRequestDtoFromItemRequest)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GetItemRequestDto getRequestById(long userId, long requestId) {
        userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        return ItemRequestMapper.toGetItemRequestDtoFromItemRequest(
                itemRequestStorage.findById(requestId).orElseThrow(() ->
                        new NotFoundException("Запрос на вещь не найден")
                ));
    }
}
