package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addFilm(Film film) throws FilmNotFound {
        filmStorage.addFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long filmId) throws FilmNotFound {
        return filmStorage.getFilmById(filmId);
    }

    public Film updateFilm(Film film) throws FilmNotFound {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(long filmId) throws FilmNotFound {
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void setFilmGenres(long filmId, List<Genre> genres) {
        filmStorage.setFilmGenres(filmId, genres);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return filmStorage.getFilmGenres(filmId);
    }

    public List<Film> getAllFilmsByDirector(int directorId, String sortBy) {
        return filmStorage.getAllFilmsByDirector(directorId, sortBy);
    }

}
