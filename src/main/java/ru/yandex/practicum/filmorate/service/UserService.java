package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long userId) throws UserNotFound {
       return userStorage.getUserById(userId);
    }

    public void updateUser(User user) throws UserNotFound {
        userStorage.updateUser(user);
    }

    public void deleteUser(long id) throws UserNotFound {
        userStorage.deleteUser(id);
    }

}
