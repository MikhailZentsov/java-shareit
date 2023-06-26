package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<GetUserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public GetUserDto getById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public GetUserDto create(@RequestBody @Valid CreateUpdateUserDto createUpdateUserDto) {
        return userService.create(createUpdateUserDto);
    }

    @PatchMapping("/{userId}")
    @Validated(OnUpdate.class)
    public GetUserDto update(@PathVariable long userId,
                             @RequestBody @Valid CreateUpdateUserDto createUpdateUserDto) {
        return userService.update(userId, createUpdateUserDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);
    }
}
