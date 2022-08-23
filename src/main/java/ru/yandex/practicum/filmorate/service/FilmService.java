package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    public List<Film> getPopularFilms(int count, int genreId, int year) {
        if (genreId == 0 && year == 0) {
            return filmStorage.getPopularFilms(count);
        } else if (genreId != 0 && year == 0) {
            return filmStorage.getPopularByGenre(genreId);
        } else if (genreId == 0){
            return filmStorage.getPopularFilmsByYear(year);
        } else {
            return filmStorage.getPopularFilmsByGenreAndYear(count, genreId, year);
        }
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

    public Collection<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public Collection<Film> getSearchFilms(String query,String by) {
        if (Objects.equals(by, "title,director") || Objects.equals(by, "director,title")) {
            return filmStorage.getSearchFilmsByTittleAndDirector(query);
        } else if ( Objects.equals(by, "title")) {
            return filmStorage.getSearchFilmsByTittle(query);
        } else if (Objects.equals(by, "director")){
            return filmStorage.getSearchFilmsByDirector(query);
        }
        return null;
    }

}
