package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {

    @Query("select r from ItemRequest r " +
            "join fetch r.requester u " +
            "left join fetch r.items i " +
            "where u = :user ")
    List<ItemRequest> getAllByRequester(@Param("user") User requester, Sort sort);

    @Query(value = "select r from ItemRequest r " +
            "join fetch r.requester u " +
            "left join fetch r.items i " +
            "where u != :user ",
    countQuery = "select r from ItemRequest r " +
            "where r.requester != :user ")
    List<ItemRequest> getAllByRequesterNot(@Param("user") User requester, Pageable pageable);
}
