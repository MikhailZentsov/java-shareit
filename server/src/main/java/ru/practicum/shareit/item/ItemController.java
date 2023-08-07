package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.marker.ToLog;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@ToLog
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<GetItemDto> getAllByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                           @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
                                           @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return itemService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public GetItemDto getByItemId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                  @PathVariable long itemId) {
        return itemService.getOneById(userId, itemId);
    }

    @PostMapping
    public GetItemDto create(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                             @RequestBody @Validated(OnCreate.class) CreateUpdateItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public GetItemDto update(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                             @PathVariable long itemId,
                             @RequestBody @Validated(OnUpdate.class) CreateUpdateItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                       @PathVariable long itemId) {
        itemService.delete(userId, itemId);
    }

    @GetMapping("/search")
    public List<GetItemDto> search(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                   @RequestParam @NotBlank String text,
                                   @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
                                   @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return itemService.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public GetCommentDto createComment(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                       @PathVariable long itemId,
                                       @RequestBody @Valid CreateCommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }
}
