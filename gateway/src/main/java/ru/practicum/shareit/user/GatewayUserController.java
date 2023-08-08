package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;
import ru.practicum.shareit.marker.ToLog;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@ToLog
public class GatewayUserController {
    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
            @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return client.getAll(from, size);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        return client.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(OnCreate.class) CreateUpdateUserDto createUpdateUserDto) {
        return client.create(createUpdateUserDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId,
                             @RequestBody @Validated(OnUpdate.class) CreateUpdateUserDto createUpdateUserDto) {
        return client.update(userId, createUpdateUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable long userId) {
        return client.deleteById(userId);
    }
}
