package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addEvent(long userId, EventType eventType, Operation operation, long entityId) throws UserNotFound {
        String sql = "INSERT INTO EVENTS (EVENT_DATE, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"EVENT_ID"});
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, userId);
            ps.setString(3, eventType.name());
            ps.setString(4, operation.name());
            ps.setLong(5, entityId);
            return ps;
        });
    }

    @Override
    public List<Event> getAllEvents(long id) throws UserNotFound {
        try {
            String sql = "SELECT * FROM EVENTS WHERE USER_ID = ? ORDER BY EVENT_DATE DESC";
            return jdbcTemplate.query(sql, new EventRowMapper(), id);
        } catch (RuntimeException e) {
            throw new UserNotFound("");
        }
    }

}
