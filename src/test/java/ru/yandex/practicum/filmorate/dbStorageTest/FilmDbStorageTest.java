package ru.yandex.practicum.filmorate.dbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanup() {
        jdbcTemplate.update("DELETE FROM FILMS");
    }

    @Test
    public void testAddFilm() {
        Film film = getTestFilm();
        Film actualFilm = filmDbStorage.addFilm(film);
        Assertions.assertEquals(film, actualFilm);
    }

    @Test
    public void testGetFilmById() {
        Film film = getTestFilm();
        long id = filmDbStorage.addFilm(film).getId();
        Film actualFilm = filmDbStorage.getFilmById(id);
        Assertions.assertEquals(film, actualFilm);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> expectedFilms = new ArrayList<>();
        Film film = getTestFilm();
        film.setId(1L);
        expectedFilms.add(film);
        film.setId(2L);
        expectedFilms.add(film);
        film.setId(3L);
        expectedFilms.add(film);

        filmDbStorage.addFilm(film);
        filmDbStorage.addFilm(film);
        filmDbStorage.addFilm(film);

        List<Film> actualFilms = filmDbStorage.getAllFilms();
        Assertions.assertEquals(3, actualFilms.size());
        Assertions.assertTrue(actualFilms.containsAll(expectedFilms));
    }

    @Test
    public void testUpdateFilm() {
        Film film = getTestFilm();
        long id = filmDbStorage.addFilm(film).getId();
        Film actualFilm = filmDbStorage.getFilmById(id);
        filmDbStorage.updateFilm(film);
        Assertions.assertEquals(film, actualFilm);
    }

    @Test
    public void testDeleteUser() {
        Film film = getTestFilm();
        filmDbStorage.addFilm(film);
        filmDbStorage.deleteFilm(film.getId());
        Assertions.assertEquals(0, filmDbStorage.getAllFilms().size());
    }

    private Film getTestFilm() {
        Film film = new Film();
        film.setName("Test Film Name");
        film.setDescription("Test Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setMpa(getTestMpa());
        return film;
    }

    private Mpa getTestMpa() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("Test MPA");
        return mpa;
    }
}
