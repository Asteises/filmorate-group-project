package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.utils.DateAfter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Film {

    private long id;
    @NotBlank
    @NotNull
    private String name;
    private List<Genre> genres = new ArrayList<>();
    @NotNull
    private Mpa mpa;
    @Size(max = 200, message = "не может быть больше 200")
    private String description;
    @DateAfter
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Integer rate;
    private List<Director> directors = new ArrayList<>();

}
