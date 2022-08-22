package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;
    private final EventService eventService;

    /**
     * Добавляем нового User
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    /**
     * Получаем всех User
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Получаем User по id
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) throws UserNotFound {
        return userService.getUserById(id);
    }

    /**
     * Изменяем существующего User
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws UserNotFound {
        return userService.updateUser(user);
    }

    /**
     * Удаляем User по id
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) throws UserNotFound {
        userService.deleteUser(userId);
    }

    /**
     * Добавляем User в друзья к другому User
     */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFound {
        friendService.addFriend(id, friendId);
        eventService.addEvent(id, EventType.FRIEND, Operation.ADD, friendId);
        return userService.getUserById(id);
    }

    /**
     * Удаляем User из друзей другого User
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFound {
        friendService.deleteFriend(friendId, id);
        eventService.addEvent(id, EventType.FRIEND, Operation.REMOVE, friendId);
    }

    /**
     * Получаем всех друзей User по id
     */
    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) throws UserNotFound {
        return friendService.getAllFriends(id);
    }

    /**
     * Получаем всех общих друзей двух User
     */
    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getAllCommonFriends(@PathVariable long userId, @PathVariable long friendId) throws UserNotFound {
        return friendService.getAllCommonFriends(userId, friendId);
    }

    /**
     * Возвращаем ленту событий пользователя
     */
    @GetMapping("/{id}/feed")
    public List<Event> getAllEvents(@PathVariable long id) {
        return eventService.getAllEvents(id);
    }

}
