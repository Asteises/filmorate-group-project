package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Получаем User по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) throws UserNotFound {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Изменяем существующего User
     */
    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws UserNotFound {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    /**
     * Удаляем User по id
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) throws UserNotFound {
        userService.deleteUser(userId);
        return ResponseEntity.ok("");
    }

    /**
     * Добавляем User в друзья к другому User
     */
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFound {
        friendService.addFriend(id, friendId);
        eventService.addEvent(id, EventType.FRIEND, Operation.ADD, friendId);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Удаляем User из друзей другого User
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFound {
        friendService.deleteFriend(friendId, id);
        eventService.addEvent(id, EventType.FRIEND, Operation.REMOVE, friendId);
        return ResponseEntity.ok("Friend has been deleted");
    }

    /**
     * Получаем всех друзей User по id
     */
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable long id) throws UserNotFound {
        return new ResponseEntity<>(friendService.getAllFriends(id), HttpStatus.OK);
    }

    /**
     * Получаем всех общих друзей двух User
     */
    @GetMapping("/{userId}/friends/common/{friendId}")
    public ResponseEntity<List<User>> getAllCommonFriends(@PathVariable long userId, @PathVariable long friendId) throws UserNotFound {
        return new ResponseEntity<>(friendService.getAllCommonFriends(userId, friendId), HttpStatus.OK);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getAllEvents(@PathVariable long id) {
        return eventService.getAllEvents(id);
    }

}
