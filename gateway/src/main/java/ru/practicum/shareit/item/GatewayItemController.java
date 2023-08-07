package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;
import ru.practicum.shareit.marker.ToLog;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.ArrayList;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@ToLog
public class GatewayItemController {
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                           @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
                                           @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return client.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByItemId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                  @PathVariable long itemId) {
        return client.getOneById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                             @RequestBody @Validated(OnCreate.class) CreateUpdateItemDto itemDto) {
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                             @PathVariable long itemId,
                             @RequestBody @Validated(OnUpdate.class) CreateUpdateItemDto itemDto) {
        return client.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                       @PathVariable long itemId) {
        client.delete(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                   @RequestParam String text,
                                   @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
                                   @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        if (text.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        return client.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                       @PathVariable long itemId,
                                       @RequestBody @Valid CreateCommentDto commentDto) {
        return client.createComment(userId, itemId, commentDto);
    }
}
