package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final LikesService likesService;
    private final EventService eventService;

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
    public ResponseEntity<List<Film>> getAllFilms() {
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.OK);
    }

    /**
     * Получаем Film по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable long id) throws FilmNotFound {
        return new ResponseEntity<>(filmService.getFilmById(id), HttpStatus.OK);
    }

    /**
     * Изменяем существующий Film
     */
    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws FilmNotFound {
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    /**
     * Удаляем Film по id
     */
    @DeleteMapping("/{filmId}")
    public ResponseEntity<String> deleteFilm(@PathVariable long filmId) throws FilmNotFound {
        filmService.deleteFilm(filmId);
        return ResponseEntity.ok("Film delete");
    }

    /**
     * Получаем Film по популярности(количеству like) по жанру и годам
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "0") int count,
                                                      @RequestParam(defaultValue = "0") int genreId,
                                                      @RequestParam(required = false) String year) {
       return ResponseEntity.ok(filmService.getPopularFilms(count, genreId, year));
    }

    /**
     * Вывод общих с другом фильмов с сортировкой по их популярности
     * */
    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    /**
     * Добавляем Genre в Film
     */
    @PostMapping("{id}/genres/set/")
    public ResponseEntity<String> setGenres(@PathVariable long id, @RequestBody List<Genre> genres) throws FilmNotFound {
        filmService.setFilmGenres(id, genres);
        return ResponseEntity.ok("Genres успешно добавлены");
    }

    /**
     * Получаем все Genre у Film
     */
    @GetMapping("{id}/genres/")
    public ResponseEntity<List<Genre>> getFilmGenres(@PathVariable long id) throws FilmNotFound {
        return ResponseEntity.ok(filmService.getFilmGenres(id));
    }

    /**
     * Добавляем like к Film
     */
    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable long userId, @PathVariable long filmId) throws UserNotFound, FilmNotFound {
        eventService.addEvent(userId, EventType.LIKE, Operation.ADD, filmId);
        likesService.addLikeToFilm(userId, filmId);
        return ResponseEntity.ok("Лайк добавлен");
    }

    /**
     * Удаляем like у Film
     */
    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> deleteLikeFromFilm(@PathVariable long filmId, @PathVariable long userId) throws UserNotFound, FilmNotFound {
        eventService.addEvent(userId, EventType.LIKE, Operation.REMOVE, filmId);
        likesService.deleteLikeFromFilm(filmId, userId);
        return ResponseEntity.ok("Like delete");
    }

    /**
     * Выводим все Film от заданного Director по годам или лайкам
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getAllFilmsByDirectorSortByYearOrLikes(@PathVariable int directorId, @RequestParam String sortBy) {
        return filmService.getAllFilmsByDirector(directorId, sortBy);
    }

    /**
     * Поиск Film по названию и режиссёру
     * */
    @GetMapping("/search")
    public Collection<Film> getSearchFilms(@RequestParam String query, @RequestParam(required = false) String by) {
        return filmService.getSearchFilms(query, by);
    }
}
