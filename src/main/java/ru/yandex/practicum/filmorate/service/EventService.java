package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventStorage eventStorage;

    public void addEvent(long userId, EventType eventType, Operation operation, long entityId) {
        eventStorage.addEvent(userId, eventType, operation, entityId);
    }

    public List<Event> getAllEvents(long id) {
        return eventStorage.getAllEvents(id);
    }

}
