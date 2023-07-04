package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.MethodArgumentException;
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
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public List<GetBookingDto> getUserBookings(long userId, String stateString) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        State state;

        state = State.valueOf(stateString.toUpperCase());
        LocalDateTime currentMoment = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingStorage.findByBooker(user)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingStorage.findByBookerAndStartDateBeforeAndEndDateAfter(user, currentMoment, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case PAST:
                return bookingStorage.findByBookerAndEndDateBefore(user, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingStorage.findByBookerAndStartDateAfter(user, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingStorage.findByBookerAndStatus(user, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingStorage.findByBookerAndStatus(user, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<GetBookingDto> getOwnerBookings(long userId, String stateString) {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        State state;

        state = State.valueOf(stateString.toUpperCase());
        LocalDateTime currentMoment = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingStorage.findByItemOwner(user)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingStorage.findByItemOwnerAndStartDateBeforeAndEndDateAfter(user, currentMoment, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case PAST:
                return bookingStorage.findByItemOwnerAndEndDateBefore(user, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingStorage.findByItemOwnerAndStartDateAfter(user, currentMoment)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingStorage.findByItemOwnerAndStatus(user, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingStorage.findByItemOwnerAndStatus(user, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toGetBookingDtoFromBooking)
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

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
    public GetBookingDto approveBooking(long userId, long bookingId, String approved) {
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

        if (approved.equals("true")) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new NotAvailableException("Бронирование уже подтверждено");
            }
            status = Status.APPROVED;
        } else if (approved.equals("false")) {
            status = Status.REJECTED;
        } else {
            throw new MethodArgumentException(String.format("Статус %s подтверждения не корректный", approved));
        }

        booking.setStatus(status);

        return BookingMapper.toGetBookingDtoFromBooking(bookingStorage.save(booking));
    }
}
