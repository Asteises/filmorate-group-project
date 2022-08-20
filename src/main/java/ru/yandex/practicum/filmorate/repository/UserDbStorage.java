package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(birthday));
            }
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User getUserById(long id) throws UserNotFound {
        try {
            String sql = "SELECT * FROM USERS " +
                    "WHERE ID = ?";
            String sqlFriends = "SELECT * FROM USERS " +
                    "WHERE ID IN " +
                    "(SELECT FRIEND_ID FROM FRIENDS " +
                    "WHERE USER_ID = ? " +
                    "AND STATUS IS TRUE)";
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
            List<User> userFriends = jdbcTemplate.query(sqlFriends, new UserRowMapper(), id);
            user.setFriends(userFriends);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFound("");
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFound {
            getUserById(user.getId());
            String sql = "UPDATE USERS SET " +
                    "EMAIL = ?, " +
                    "LOGIN = ?, " +
                    "NAME = ?, " +
                    "BIRTHDAY = ? " +
                    "WHERE ID = ?";
            jdbcTemplate.update(sql,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );
            return user;
    }

    @Override
    public void deleteUser(long id) throws UserNotFound {
        /**
         * Добавленный код
         *         String sqlLikes = "DELETE FROM LIKES WHERE USER_ID = ?";
         *         String sqlEvents = "DELETE FROM EVENTS WHERE USER_ID = ?";
         *         String sqlFriends = "DELETE FROM FRIENDS WHERE USER_ID = ?";
         *         String sqlFiendsAsFriendId = "DELETE FROM FRIENDS WHERE FRIEND_ID = ?";
         *
         *          jdbcTemplate.update(sqlFriends, id);
         *         jdbcTemplate.update(sqlFiendsAsFriendId, id);
         *         jdbcTemplate.update(sqlLikes, id);
         *         jdbcTemplate.update(sqlEvents, id);
         * */
        String sqlLikes = "DELETE FROM LIKES WHERE USER_ID = ?";
        String sqlEvents = "DELETE FROM EVENTS WHERE USER_ID = ?";
        String sqlFriends = "DELETE FROM FRIENDS WHERE USER_ID = ?";
        String sqlFiendsAsFriendId = "DELETE FROM FRIENDS WHERE FRIEND_ID = ?";
        String sql = "DELETE FROM USERS WHERE ID = ?";

        jdbcTemplate.update(sqlFriends, id);
        jdbcTemplate.update(sqlFiendsAsFriendId, id);
        jdbcTemplate.update(sqlLikes, id);
        jdbcTemplate.update(sqlEvents, id);
        jdbcTemplate.update(sql, id);
    }
}
