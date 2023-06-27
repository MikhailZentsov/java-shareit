package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
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

        return Collections.emptyList();
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
        final Long ownerId = item.getOwner().getId();
        final Long itemId = item.getId();

        items.put(itemId, item);
        itemsByUser.computeIfAbsent(ownerId, k -> new HashMap<>()).put(itemId, item);

        return getOneById(itemId);
    }

    @Override
    public Item update(long userId, Item item) {
        getOneById(item.getId());
        final Long ownerId = items.get(item.getId()).getOwner().getId();
        final Long itemId = item.getId();

        if (!ownerId.equals(userId)) {
            throw new NotFoundException("Вещь не найдена");
        }

        items.put(itemId, item);
        itemsByUser.get(ownerId).put(itemId, item);

        return getOneById(itemId);
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
        String lowerCaseText = text.toLowerCase();

        return items.values()
                .stream()
                .filter(t -> (t.getName().toLowerCase().contains(lowerCaseText)
                        || t.getDescription().toLowerCase().contains(lowerCaseText))
                        && t.getAvailable())
                .collect(Collectors.toList());
    }

    private long getNewId() {
        return id++;
    }
}
