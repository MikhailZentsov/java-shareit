package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.validator.ValuesAllowedConstraint;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<GetBookingDto> getUserBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @ValuesAllowedConstraint(propName = "state",
                                                       values = {"all",
                                                               "current",
                                                               "past",
                                                               "future",
                                                               "waiting",
                                                               "rejected"},
                                                       message = "Unknown state: UNSUPPORTED_STATUS")
                                               @RequestParam(required = false, defaultValue = "all") String state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<GetBookingDto> getOwnerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @ValuesAllowedConstraint(propName = "state",
                                                        values = {"all",
                                                                "current",
                                                                "past",
                                                                "future",
                                                                "waiting",
                                                                "rejected"},
                                                        message = "Unknown state: UNSUPPORTED_STATUS")
                                                @RequestParam(required = false, defaultValue = "all") String state) {
        return bookingService.getOwnerBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public GetBookingDto getBookingByUserOwner(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @PathVariable long bookingId) {
        return bookingService.getBookingByUserOwner(userId, bookingId);
    }

    @PostMapping
    public GetBookingDto create(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                @RequestBody @Valid CreateBookingDto createBookingDto) {
        return bookingService.create(userId, createBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public GetBookingDto approveBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                        @PathVariable long bookingId,
                                        @ValuesAllowedConstraint(propName = "approved",
                                                values = {"true", "false"})
                                        @RequestParam String approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
