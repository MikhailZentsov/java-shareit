package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class CreateUpdateItemDto {

    @NotBlank(groups = OnCreate.class)
    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 512, groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(groups = OnCreate.class)
    private Boolean available;

    private Long requestId;
}
