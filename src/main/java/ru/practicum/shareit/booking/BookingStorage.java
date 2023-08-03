package ru.practicum.shareit.booking;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.id = :id ")
    @NotNull Optional<Booking> findById(@Param("id") @NotNull Long id);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item " +
            "where bk = :user ",
    countQuery = "select b from Booking b " +
            "where b.booker = :user ")
    List<Booking> findAllByBooker(@Param("user") User booker, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item " +
            "where bk = :user " +
            "   and b.startDate < :time " +
            "   and b.endDate > :time",
            countQuery = "select b from Booking b " +
                    "where b.booker = :user " +
                    "   and b.startDate < :time " +
                    "   and b.endDate > :time")
    List<Booking> findAllByBookerAndCurrent(@Param("user") User booker, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item " +
            "where bk = :user " +
            "   and b.endDate < :time",
            countQuery = "select b from Booking b " +
                    "where b.booker = :user " +
                    "   and b.endDate < :time")
    List<Booking> findAllByBookerAndPast(@Param("user") User booker, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item " +
            "where bk = :user " +
            "   and b.startDate > :time",
            countQuery = "select b from Booking b " +
                    "where b.booker = :user " +
                    "   and b.startDate > :time")
    List<Booking> findAllByBookerAndFuture(@Param("user") User booker, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item " +
            "where bk = :user " +
            "   and b.status = :status",
            countQuery = "select b from Booking b " +
                    "where b.booker = :user " +
                    "   and b.status = :status")
    List<Booking> findAllByBookerAndStatus(@Param("user") User booker, @Param("status") Status status, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.item.owner = :user ",
            countQuery = "select b from Booking b " +
                    "where b.item.owner= :user ")
    List<Booking> findAllByItemOwner(@Param("user") User itemOwner, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.startDate < :time " +
            "   and b.endDate > :time",
            countQuery = "select b from Booking b " +
                    "where b.item.owner = :user " +
                    "   and b.startDate < :time " +
                    "   and b.endDate > :time")
    List<Booking> findAllByItemOwnerAndCurrent(@Param("user") User itemOwner, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.endDate < :time",
            countQuery = "select b from Booking b " +
                    "where b.item.owner = :user " +
                    "   and b.endDate < :time")
    List<Booking> findAllByItemOwnerAndPast(@Param("user") User itemOwner, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.startDate > :time",
            countQuery = "select b from Booking b " +
                    "where b.item.owner = :user " +
                    "   and b.startDate > :time")
    List<Booking> findAllByItemOwnerAndFuture(@Param("user") User itemOwner, @Param("time") LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "join fetch b.booker bk " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.status = :status",
            countQuery = "select b from Booking b " +
                    "where b.item.owner = :user " +
                    "   and b.status = :status")
    List<Booking> findAllByItemOwnerAndStatus(@Param("user") User itemOwner, @Param("status") Status status, Pageable pageable);
}
