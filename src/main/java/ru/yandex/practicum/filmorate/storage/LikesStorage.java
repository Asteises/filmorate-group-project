package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;

@Repository
public interface LikesStorage {

    void addLike(long userId, long filmId);

    void deleteLikeFromFilm(long filmId, long userId);

    Integer getLikesCount(long id);

}
