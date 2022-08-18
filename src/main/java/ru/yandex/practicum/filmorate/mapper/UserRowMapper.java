package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    /**
     * Получаем User из строки базы данных
     */
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("ID"));
        user.setLogin(rs.getString("LOGIN"));
        user.setEmail(rs.getString("EMAIL"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(rs.getTimestamp("BIRTHDAY").toLocalDateTime().toLocalDate());
        return user;
    }

}
