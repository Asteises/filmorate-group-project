package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Getter
@Setter
@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    @Override
    public void addFriend(long userId, long friendId) throws UserNotFound {
        userService.getUserById(userId);
        userService.getUserById(friendId);
        String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, Boolean.TRUE);
    }

    @Override
    public void deleteFriend(long userId, long friendId) throws UserNotFound {
        userService.getUserById(userId);
        userService.getUserById(friendId);
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        jdbcTemplate.update(sql, friendId, userId);
    }

    @Override
    public List<User> getAllFriends(long id) throws UserNotFound {
        userService.getUserById(id);
        String sqlFriend = "SELECT * FROM USERS " +
                "LEFT JOIN FRIENDS ON USERS.ID=FRIENDS.FRIEND_ID " +
                "WHERE FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlFriend, new UserRowMapper(), id);
    }

    @Override
    public List<User> getAllCommonFriends(long userId, long otherUserId) throws UserNotFound {
        userService.getUserById(userId);
        userService.getUserById(otherUserId);
        String sqlFriend = "SELECT * FROM " +
                "(SELECT * FROM USERS LEFT JOIN FRIENDS ON USERS.ID=FRIENDS.FRIEND_ID WHERE FRIENDS.USER_ID = ?) t1 " +
                "JOIN " +
                "(SELECT * FROM USERS LEFT JOIN FRIENDS ON USERS.ID=FRIENDS.FRIEND_ID WHERE FRIENDS.USER_ID = ?) t2 " +
                "ON t2.ID=t1.ID";
        return jdbcTemplate.query(sqlFriend, new UserRowMapper(), userId, otherUserId);
    }

}
