package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "eventId") // Объект определяется только по полю eventId
public class Event {

    private long eventId;
    private Long timestamp;
    private long userId;
    private EventType eventType;
    private Operation operation;
    private long entityId;

}
