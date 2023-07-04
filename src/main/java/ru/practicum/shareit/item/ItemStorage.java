package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage extends JpaRepository<Item, Long> {

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.owner.id = :id ")
    List<Item> findAllyOwnerIdWithBookings(@Param("id") Long userId);

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.id = :id ")
    @NonNull
    Optional<Item> findByIdWithOwner(@Param("id") @NonNull Long id);

    @Query(" select i from Item i " +
            "where lower(i.name) like lower(concat('%', :text, '%')) " +
            "   or lower(i.description) like lower(concat('%', :text, '%')) " +
            "   and i.available = true ")
    List<Item> search(@Param("text") String text);
}
