package ru.practicum.shareit.mapper;

import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static GetUserDto toGetUserDtoFromUser(User user) {
        return GetUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUserFromCreateUpdateUserDto(CreateUpdateUserDto createUpdateUserDto) {
        return User.builder()
                .name(createUpdateUserDto.getName())
                .email(createUpdateUserDto.getEmail())
                .build();
    }
}
