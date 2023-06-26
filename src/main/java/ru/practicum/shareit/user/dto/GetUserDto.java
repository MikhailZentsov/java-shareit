package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserDto {
    private Long id;
    private String name;
    private String email;
}
