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
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.marker.ToLog;
import ru.practicum.shareit.validator.ValuesAllowedConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@ToLog
public class GatewayBookingController {
    private final BookingClient client;

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @ValuesAllowedConstraint(propName = "state",
                                                       values = {"all",
                                                               "current",
                                                               "past",
                                                               "future",
                                                               "waiting",
                                                               "rejected"},
                                                       message = "Unknown state: UNSUPPORTED_STATUS")
                                               @RequestParam(defaultValue = "all") String state,
                                               @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int from,
                                               @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return client.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @ValuesAllowedConstraint(propName = "state",
                                                        values = {"all",
                                                                "current",
                                                                "past",
                                                                "future",
                                                                "waiting",
                                                                "rejected"},
                                                        message = "Unknown state: UNSUPPORTED_STATUS")
                                                @RequestParam(defaultValue = "all") String state,
                                                @RequestParam(defaultValue = "0") @Min(0) int from,
                                                @RequestParam(defaultValue = "20") @Min(1) @Max(20) int size) {
        return client.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByUserOwner(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                               @PathVariable long bookingId) {
        return client.getBookingByUserOwner(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                @RequestBody @Valid CreateBookingDto createBookingDto) {
        return client.create(userId, createBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                        @PathVariable long bookingId,
                                        @RequestParam Boolean approved) {
        return client.approveBooking(userId, bookingId, approved);
    }
}
