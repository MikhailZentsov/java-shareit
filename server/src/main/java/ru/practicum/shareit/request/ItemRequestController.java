package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.marker.ToLog;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@ToLog
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public GetItemRequestDto createRequest(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                           @RequestBody CreateItemRequestDto itemRequestDto) {
        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<GetItemRequestDto> getAllRequestsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId) {
        return itemRequestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<GetItemRequestDto> getAllRequests(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "20") int size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public GetItemRequestDto getRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                            @PathVariable long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
