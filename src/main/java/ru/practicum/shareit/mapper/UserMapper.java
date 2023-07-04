package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetBookingUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public GetUserDto toGetUserDtoFromUser(User user) {
        return GetUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUserFromCreateUpdateUserDto(CreateUpdateUserDto createUpdateUserDto) {
        return User.builder()
                .name(createUpdateUserDto.getName())
                .email(createUpdateUserDto.getEmail())
                .build();
    }

    public GetBookingUserDto toGetBookingUserDtoFromUser(User user) {
        return GetBookingUserDto.builder()
                .id(user.getId())
                .build();
    }
}
