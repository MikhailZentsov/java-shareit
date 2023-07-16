package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    GetItemRequestDto createRequest(long userId, CreateItemRequestDto itemRequestDto);

    List<GetItemRequestDto> getAllRequestsByUserId(long userId);

    List<GetItemRequestDto> getAllRequests(long userId, int from, int size);

    GetItemRequestDto getRequestById(long userId, long requestId);
}
