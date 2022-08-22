package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository
public interface EventStorage {

   void addEvent(long userId, EventType eventType, Operation operation, long entityId);

   List<Event> getAllEvents(long id);

}
