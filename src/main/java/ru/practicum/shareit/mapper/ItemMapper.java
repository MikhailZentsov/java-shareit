package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetBookingForItemDto;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.orderByCreatedDesc;
import static ru.practicum.shareit.util.Constants.orderByStartDateAsc;
import static ru.practicum.shareit.util.Constants.orderByStartDateDesc;

@UtilityClass
public class ItemMapper {
    public GetItemDto toGetItemDtoFromItem(Item item) {
        SortedSet<GetCommentDto> comments = new TreeSet<>(orderByCreatedDesc);

        if (item.getComments() != null) {
            comments.addAll(item.getComments()
                    .stream()
                    .map(CommentMapper::toGetCommentDtoFromComment)
                    .collect(Collectors.toSet()));
        }

        return GetItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .build();
    }

    public GetItemDto toGetItemWIthBookingDtoFromItem(Item item) {
        LocalDateTime currentTime = LocalDateTime.now();

        GetItemDto getItemDto = toGetItemDtoFromItem(item);

        Set<Booking> bookings = item.getBookings();

        Booking lastBooking = bookings
                .stream()
                .sorted(orderByStartDateDesc)
                .filter(t -> t.getStartDate().isBefore(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst()
                .orElse(null);

        Booking nextBooking = bookings
                .stream()
                .sorted(orderByStartDateAsc)
                .filter(t -> t.getStartDate().isAfter(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst()
                .orElse(null);

        getItemDto.setLastBooking(BookingMapper.toGetItemBookingDtoFromBooking(lastBooking));
        getItemDto.setNextBooking(BookingMapper.toGetItemBookingDtoFromBooking(nextBooking));

        return getItemDto;
    }

    public Item toGetItemFromCreateUpdateItemDto(CreateUpdateItemDto createUpdateItemDto) {
        return Item.builder()
                .name(createUpdateItemDto.getName())
                .description(createUpdateItemDto.getDescription())
                .available(createUpdateItemDto.getAvailable())
                .build();
    }

    public GetBookingForItemDto toGetBookingDtoFromItem(Item item) {
        return GetBookingForItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
