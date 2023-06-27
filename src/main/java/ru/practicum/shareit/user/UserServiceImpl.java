package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<GetUserDto> getAll() {
        return userStorage.getAll()
                .stream()
                .map(UserMapper::toGetUserDtoFromUser)
                .collect(Collectors.toList());
    }

    @Override
    public GetUserDto getById(long id) {
        return UserMapper.toGetUserDtoFromUser(userStorage.getById(id));
    }

    @Override
    public GetUserDto create(CreateUpdateUserDto createUpdateUserDto) {
        return UserMapper.toGetUserDtoFromUser(
                userStorage.create(UserMapper.toUserFromCreateUpdateUserDto(createUpdateUserDto))
        );
    }

    @Override
    public GetUserDto update(long id, CreateUpdateUserDto createUpdateUserDto) {
        User user = userStorage.getById(id);

        if (createUpdateUserDto.getName() != null && !createUpdateUserDto.getName().isBlank()) {
            user.setName(createUpdateUserDto.getName());
        }

        if (createUpdateUserDto.getEmail() != null  && !createUpdateUserDto.getEmail().isBlank()) {
            user.setEmail(createUpdateUserDto.getEmail());
        }

        return UserMapper.toGetUserDtoFromUser(userStorage.update(user));
    }

    @Override
    public void deleteById(long id) {
        userStorage.deleteById(id);
    }
}
