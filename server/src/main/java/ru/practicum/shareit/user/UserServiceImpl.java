package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.SORT_BY_ID_ASC;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Transactional(readOnly = true)
    @Override
    public List<GetUserDto> getAll(int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, SORT_BY_ID_ASC);
        return userStorage.findAll(pageable)
                .getContent()
                .stream()
                .map(UserMapper::toGetUserDtoFromUser)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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
        } catch (DataIntegrityViolationException e) {
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

        try {
            return UserMapper.toGetUserDtoFromUser(
                    userStorage.saveAndFlush(user)
            );
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "Пользователь с %s уже зарегистрирован", createUpdateUserDto.getEmail()
            ));
        }
    }

    @Override
    public void deleteById(long id) {
        userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        userStorage.deleteById(id);
    }
}
