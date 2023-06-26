package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUpdateItemDto {

    @NotBlank(groups = OnCreate.class)
    private String name;

    @NotBlank(groups = OnCreate.class)
    private String description;

    @NotNull(groups = OnCreate.class)
    private Boolean available;
}
