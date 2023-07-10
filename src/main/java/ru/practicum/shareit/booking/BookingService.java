package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;

import java.util.List;

public interface BookingService {

    List<GetBookingDto> getUserBookings(long userId, String state);

    List<GetBookingDto> getOwnerBookings(long userId, String state);

    GetBookingDto getBookingByUserOwner(long userId, long bookingId);

    GetBookingDto create(long userid, CreateBookingDto createBookingDto);

    GetBookingDto approveBooking(long userId, long bookingId, Boolean approved);
}
