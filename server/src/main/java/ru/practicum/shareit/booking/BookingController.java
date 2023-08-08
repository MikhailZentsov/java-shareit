package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.marker.ToLog;

import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@ToLog
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<GetBookingDto> getUserBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @RequestParam(defaultValue = "all") String state,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "20") int size) {
        return bookingService.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<GetBookingDto> getOwnerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @RequestParam(defaultValue = "all") String state,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") int size) {
        return bookingService.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public GetBookingDto getBookingByUserOwner(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @PathVariable long bookingId) {
        return bookingService.getBookingByUserOwner(userId, bookingId);
    }

    @PostMapping
    public GetBookingDto create(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                @RequestBody CreateBookingDto createBookingDto) {
        return bookingService.create(userId, createBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public GetBookingDto approveBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                        @PathVariable long bookingId,
                                        @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
