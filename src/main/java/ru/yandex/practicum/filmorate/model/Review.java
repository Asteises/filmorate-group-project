package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "reviewId") // Объект определяется только по полю reviewId
public class Review {

    private long reviewId;
    @NotBlank
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;

}
