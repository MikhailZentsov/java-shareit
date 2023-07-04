package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage extends JpaRepository<Item, Long> {

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.owner.id = ?1 ")
    List<Item> findAllyOwnerIdWithBookings(Long userId);

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.id = ?1 ")
    @NonNull
    Optional<Item> findByIdWithOwner(@NonNull Long id);

    @Query(" select i from Item i " +
            "where lower(i.name) like lower(concat('%', ?1, '%')) " +
            "   or lower(i.description) like lower(concat('%', ?1, '%')) " +
            "   and i.available = true ")
    List<Item> search(String text);
}
