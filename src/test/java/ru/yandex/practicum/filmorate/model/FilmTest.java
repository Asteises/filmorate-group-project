package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class FilmTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void validateFilmNameTest() {
        Film film = new Film();
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно быть не меньше 1")));
    }

    @Test
    public void validateFilmDescriptionTest() {
        Film film = new Film();
        film.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не может быть больше 200")));
    }

    @Test
    public void validateFilmReleaseDateTest() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1984, 12, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("DateAfter.invalid")));
    }

    @Test
    public void validateFilmDurationTest() {
        Film film = new Film();
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно быть не меньше 1")));
    }
}
