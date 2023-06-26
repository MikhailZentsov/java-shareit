package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User getById(long id);

    User create(User user);

    User update(User user);

    void deleteById(long id);
}
