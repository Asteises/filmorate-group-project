package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id") // Объект определяется только по полю id
@ToString
public class Mpa {

    private int id;
    private String name;

}
