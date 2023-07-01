package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> users;
    private final Set<String> emails;
    private long id;

    public InMemoryUserStorageImpl() {
        this.users = new HashMap<>();
        this.emails = new HashSet<>();
        this.id = 1;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(long id) {
        if (users.containsKey(id)) {
            return users.get(id).toBuilder().build();
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }
        user.setId(getNewId());

        users.put(user.getId(), user);
        emails.add(user.getEmail());

        return getById(user.getId());
    }

    @Override
    public User update(User user) {
        if (emails.contains(user.getEmail())
                && !users.get(user.getId()).getEmail().equals(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }

        emails.remove(users.get(user.getId()).getEmail());

        users.put(user.getId(), user);
        emails.add(user.getEmail());

        return getById(user.getId());
    }

    @Override
    public void deleteById(long id) {
        User user = getById(id);

        emails.remove(user.getEmail());
        users.remove(id);
    }

    private long getNewId() {
        return id++;
    }
}
