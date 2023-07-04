package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<GetUserDto> getAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toGetUserDtoFromUser)
                .collect(Collectors.toList());
    }

    @Override
    public GetUserDto getById(long id) {
        return UserMapper.toGetUserDtoFromUser(userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        ));
    }

    @Override
    public GetUserDto create(CreateUpdateUserDto createUpdateUserDto) {
        try {
            return UserMapper.toGetUserDtoFromUser(
                    userStorage.save(UserMapper.toUserFromCreateUpdateUserDto(createUpdateUserDto))
            );
        } catch (ConstraintViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "Пользователь с %s уже зарегистрирован", createUpdateUserDto.getEmail()
            ));
        }
    }

    @Override
    public GetUserDto update(long id, CreateUpdateUserDto createUpdateUserDto) {
        User user = userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        if (createUpdateUserDto.getName() != null && !createUpdateUserDto.getName().isBlank()) {
            user.setName(createUpdateUserDto.getName());
        }

        if (createUpdateUserDto.getEmail() != null  && !createUpdateUserDto.getEmail().isBlank()) {
            user.setEmail(createUpdateUserDto.getEmail());
        }

        return UserMapper.toGetUserDtoFromUser(userStorage.save(user));
    }

    @Override
    public void deleteById(long id) {
        userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        userStorage.deleteById(id);
    }
}
