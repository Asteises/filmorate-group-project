package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final LikesService likesService;

    /**
     * Добавляем новый Film
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws FilmNotFound {
        filmService.addFilm(film);
        return film;
    }

    /**
     * Получаем все Film
     */
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    /**
     * Получаем Film по id
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) throws FilmNotFound {
        return filmService.getFilmById(id);
    }

    /**
     * Изменяем существующий Film
     */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFound {
        return filmService.updateFilm(film);
    }

    /**
     * Удаляем Film по id
     */
    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) throws FilmNotFound {
        filmService.deleteFilm(filmId);
    }

    /**
     * Получаем Film по популярности(количеству like)
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "0") int count) {
        return filmService.getPopularFilms(count);
    }

    /**
     * Добавляем Genre в Film
     */
    @PostMapping("{id}/genres/set/")
    public void setGenres(@PathVariable long id, @RequestBody List<Genre> genres) throws FilmNotFound {
        filmService.setFilmGenres(id, genres);
    }

    /**
     * Получаем все Genre у Film
     */
    @GetMapping("{id}/genres/")
    public List<Genre> getFilmGenres(@PathVariable long id) throws FilmNotFound {
        return filmService.getFilmGenres(id);
    }

    /**
     * Добавляем like к Film
     */
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long userId, @PathVariable long filmId) throws UserNotFound, FilmNotFound {
        likesService.addLikeToFilm(userId, filmId);
    }

    /**
     * Удаляем like у Film
     */
    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable long filmId,
                                   @PathVariable long userId) throws UserNotFound, FilmNotFound {
        likesService.deleteLikeFromFilm(filmId, userId);
    }

}
