package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items;
    private final Map<Long, Map<Long, Item>> itemsByUser;
    private long id;

    public InMemoryItemStorageImpl() {
        this.items = new HashMap<>();
        this.itemsByUser = new HashMap<>();
        this.id = 1;
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        if (itemsByUser.containsKey(userId)) {
            return List.copyOf(itemsByUser.get(userId).values());
        }

        return new ArrayList<>();
    }

    @Override
    public Item getOneById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId).toBuilder().build();
        }

        throw new NotFoundException("Вещь не найдена");
    }

    @Override
    public Item create(Item item) {
        item.setId(getNewId());

        if (!itemsByUser.containsKey(item.getOwner().getId())) {
            itemsByUser.put(item.getOwner().getId(), new HashMap<>());
        }

        items.put(item.getId(), item);
        itemsByUser.get(item.getOwner().getId()).put(item.getId(), item);

        return getOneById(item.getId());
    }

    @Override
    public Item update(long userId, Item item) {
        getOneById(item.getId());

        if (!items.get(item.getId()).getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь не найдена");
        }

        items.put(item.getId(), item);
        itemsByUser.get(item.getOwner().getId()).put(item.getId(), item);

        return getOneById(item.getId());
    }

    @Override
    public void delete(long userId, long itemId) {
        Item item = getOneById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь не найдена");
        }

        items.remove(itemId);
        itemsByUser.get(userId).remove(itemId);
    }

    @Override
    public List<Item> search(String text) {
        return List.copyOf(items.values()
                .stream()
                .filter(t -> t.getName().toLowerCase().contains(text.toLowerCase())
                        || t.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList())
        );
    }

    private long getNewId() {
        return id++;
    }
}
