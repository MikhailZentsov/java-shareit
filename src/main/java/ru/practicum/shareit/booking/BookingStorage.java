package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker(User booker, Pageable pageable);

    List<Booking> findAllByBookerAndStartDateBeforeAndEndDateAfter(User booker, LocalDateTime time1, LocalDateTime time2, Pageable pageable);

    List<Booking> findAllByBookerAndEndDateBefore(User booker, LocalDateTime currentTime, Pageable pageable);

    List<Booking> findAllByBookerAndStartDateAfter(User booker, LocalDateTime currentTime, Pageable pageable);

    List<Booking> findAllByBookerAndStatus(User booker, Status status, Pageable pageable);

    List<Booking> findAllByItemOwner(User itemOwner, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartDateBeforeAndEndDateAfter(User itemOwner, LocalDateTime time1, LocalDateTime time2, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndDateBefore(User itemOwner, LocalDateTime currentTime, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartDateAfter(User itemOwner, LocalDateTime currentTime, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatus(User itemOwner, Status status, Pageable pageable);
}
