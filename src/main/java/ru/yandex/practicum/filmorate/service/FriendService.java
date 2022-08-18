package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {

    private final FriendStorage friendStorage;

    public void addFriend(long userId, long friendId) throws UserNotFound {
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) throws UserNotFound {
        friendStorage.deleteFriend(friendId, userId);
    }

    public List<User> getAllFriends(long userId) throws UserNotFound {
        return friendStorage.getAllFriends(userId);
    }

    public List<User> getAllCommonFriends(long userId, long otherUserId) throws UserNotFound {
        return friendStorage.getAllCommonFriends(userId, otherUserId);
    }

}
