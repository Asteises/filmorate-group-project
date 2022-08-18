package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    List<User> getAllUsers();

    User getUserById(long id) throws UserNotFound;

    User updateUser(User user) throws UserNotFound;

    void deleteUser(long userId) throws UserNotFound;
}
