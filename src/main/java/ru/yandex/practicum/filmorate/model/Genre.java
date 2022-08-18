package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id") // Объект определяется только по полю id
@ToString
public class Genre {

    private long id;
    private String name;

}
