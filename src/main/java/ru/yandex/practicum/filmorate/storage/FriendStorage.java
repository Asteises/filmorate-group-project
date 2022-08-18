package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public interface FriendStorage {

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getAllFriends(long id);

    List<User> getAllCommonFriends(long userId, long otherUserId);

}
