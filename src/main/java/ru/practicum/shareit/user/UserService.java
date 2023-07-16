package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;

import java.util.List;

public interface UserService {

    List<GetUserDto> getAll(int from, int size);

    GetUserDto getById(long id);

    GetUserDto create(CreateUpdateUserDto createUpdateUserDto);

    GetUserDto update(long id, CreateUpdateUserDto createUpdateUserDto);

    void deleteById(long id);
}
