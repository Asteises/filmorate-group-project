package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Slf4j
@Service
@AllArgsConstructor
public class LikesService {

    private final LikesStorage likesStorage;
    private final UserService userService;

    public void addLikeToFilm(long userId, long filmId) throws UserNotFound, FilmNotFound {
        likesStorage.addLike(userId, filmId);
        log.info("Like has been add");
    }

    public void deleteLikeFromFilm(long filmId, long userId) throws UserNotFound, FilmNotFound {
        userService.getUserById(userId);
        likesStorage.deleteLikeFromFilm(filmId, userId);
        log.info("Like deleted");
    }

}
