package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateItemRequestDto {

    @NotBlank
    @Size(max = 1000)
    private String description;
}
