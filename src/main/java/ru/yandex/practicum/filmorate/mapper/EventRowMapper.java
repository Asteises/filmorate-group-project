package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getLong("EVENT_ID"));
        event.setLocalDateTime(rs.getTimestamp("EVENT_DATE").toLocalDateTime());
        event.setUserId(rs.getLong("USER_ID"));
        event.setEventType(EventType.valueOf(rs.getString("EVENT_TYPE")));
        event.setOperation(Operation.valueOf(rs.getString("OPERATION")));
        event.setEntityId(rs.getLong("ENTITY_ID"));
        return event;
    }

}
