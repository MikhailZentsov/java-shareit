package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidDateException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    private final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "startDate");

    @Transactional(readOnly = true)
    @Override
    public List<GetBookingDto> getUserBookings(long userId, String stateString) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        State state;

        state = State.valueOf(stateString.toUpperCase());
        LocalDateTime currentMoment = LocalDateTime.now();
        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingStorage.findByBooker(user, SORT_BY_START_DATE_DESC);
                break;
            case CURRENT:
                bookings = bookingStorage.findByBookerCurrent(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case PAST:
                bookings = bookingStorage.findByBookerPast(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case FUTURE:
                bookings = bookingStorage.findByBookerFuture(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case WAITING:
                bookings = bookingStorage.findByBookerAndStatus(user, Status.WAITING, SORT_BY_START_DATE_DESC);
                break;
            case REJECTED:
                bookings = bookingStorage.findByBookerAndStatus(user, Status.REJECTED, SORT_BY_START_DATE_DESC);
                break;
            default:
                bookings = Collections.emptyList();
        }

        return bookings
                .stream()
                .map(BookingMapper::toGetBookingDtoFromBooking)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetBookingDto> getOwnerBookings(long userId, String stateString) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        State state;

        state = State.valueOf(stateString.toUpperCase());
        LocalDateTime currentMoment = LocalDateTime.now();
        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingStorage.findByItemOwner(user, SORT_BY_START_DATE_DESC);
                break;
            case CURRENT:
                bookings = bookingStorage.findByItemOwnerCurrent(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case PAST:
                bookings = bookingStorage.findByItemOwnerPast(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case FUTURE:
                bookings = bookingStorage.findByItemOwnerFuture(user, currentMoment, SORT_BY_START_DATE_DESC);
                break;
            case WAITING:
                bookings = bookingStorage.findByItemOwnerAndStatus(user, Status.WAITING, SORT_BY_START_DATE_DESC);
                break;
            case REJECTED:
                bookings = bookingStorage.findByItemOwnerAndStatus(user, Status.REJECTED, SORT_BY_START_DATE_DESC);
                break;
            default:
                bookings = Collections.emptyList();
        }

        return bookings
                .stream()
                .map(BookingMapper::toGetBookingDtoFromBooking)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public GetBookingDto getBookingByUserOwner(long userId, long bookingId) {
        userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Бронирование не найдено")
        );

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return BookingMapper.toGetBookingDtoFromBooking(booking);
    }

    @Override
    public GetBookingDto create(long userId, CreateBookingDto createBookingDto) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        if (createBookingDto.getEnd().isBefore(createBookingDto.getStart()) ||
                createBookingDto.getEnd().isEqual(createBookingDto.getStart())) {
            throw new NotValidDateException("Дата окончания не может быть раньше или равна дате начала");
        }

        Item item = itemStorage.findById(createBookingDto.getItemId()).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (!item.getAvailable()) {
            throw new NotAvailableException("Вещь не доступна для бронирования");
        }

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Нельзя забронировать свою вещь");
        }

        Booking booking = BookingMapper.toBookingFromCreateBookingDto(createBookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        return BookingMapper.toGetBookingDtoFromBooking(bookingStorage.save(booking));
    }

    @Override
    public GetBookingDto approveBooking(long userId, long bookingId, Boolean approved) {
        userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Бронирование не найдено")
        );

        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Бронирование не найдено");
        }

        Status status;

        if (approved) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new NotAvailableException("Бронирование уже подтверждено");
            }
            status = Status.APPROVED;
        } else {
            status = Status.REJECTED;
        }

        booking.setStatus(status);

        return BookingMapper.toGetBookingDtoFromBooking(bookingStorage.save(booking));
    }
}
