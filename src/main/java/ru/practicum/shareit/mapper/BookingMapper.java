package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingForGetItemDto;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {
    public Booking toBookingFromCreateBookingDto(CreateBookingDto createBookingDto) {
        return Booking.builder()
                .startDate(createBookingDto.getStart())
                .endDate(createBookingDto.getEnd())
                .build();
    }

    public GetBookingForGetItemDto toGetBookingForItemDtoFromBooking(Booking booking) {
        if (booking == null) {
            return null;
        }

        return GetBookingForGetItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker() != null ? booking.getBooker().getId() : null)
                .build();
    }

    public GetBookingDto toGetBookingDtoFromBooking(Booking booking) {
        return GetBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getStatus())
                .booker(UserMapper.toGetBookingUserDtoFromUser(booking.getBooker()))
                .item(ItemMapper.toGetBookingDtoFromItem(booking.getItem()))
                .build();
    }
}
