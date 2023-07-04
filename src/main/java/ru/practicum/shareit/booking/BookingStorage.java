package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "join fetch i.owner " +
            "where b.id = ?1 " +
            "order by b.startDate desc")
    @NonNull Optional<Booking> findById(@NonNull Long id);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = ?1 " +
            "order by b.startDate desc")
    List<Booking> findByBooker(User user);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = ?1 " +
            "   and b.startDate < ?2 " +
            "   and b.endDate > ?3 " +
            "order by b.startDate desc")
    List<Booking> findByBookerAndStartDateBeforeAndEndDateAfter(User booker,
                                                                LocalDateTime startDate,
                                                                LocalDateTime endDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = ?1 " +
            "   and b.endDate < ?2 " +
            "order by b.startDate desc")
    List<Booking> findByBookerAndEndDateBefore(User booker, LocalDateTime endDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = ?1 " +
            "   and b.startDate > ?2 " +
            "order by b.startDate desc")
    List<Booking> findByBookerAndStartDateAfter(User booker, LocalDateTime startDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = ?1 " +
            "   and b.status = ?2 " +
            "order by b.startDate desc")
    List<Booking> findByBookerAndStatus(User booker, Status status);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = ?1 " +
            "order by b.startDate desc")
    List<Booking> findByItemOwner(User user);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = ?1 " +
            "   and b.startDate < ?2 " +
            "   and b.endDate > ?3 " +
            "order by b.startDate desc")
    List<Booking> findByItemOwnerAndStartDateBeforeAndEndDateAfter(User itemOwner, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = ?1 " +
            "   and b.endDate < ?2 " +
            "order by b.startDate desc")
    List<Booking> findByItemOwnerAndEndDateBefore(User itemOwner, LocalDateTime endDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = ?1 " +
            "   and b.startDate > ?2 " +
            "order by b.startDate desc")
    List<Booking> findByItemOwnerAndStartDateAfter(User itemOwner, LocalDateTime startDate);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = ?1 " +
            "   and b.status = ?2 " +
            "order by b.startDate desc")
    List<Booking> findByItemOwnerAndStatus(User itemOwner, Status status);
}
