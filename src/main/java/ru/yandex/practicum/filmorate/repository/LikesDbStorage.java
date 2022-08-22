package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Repository
@Data
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {

    public final JdbcTemplate jdbcTemplate;

    private final EventService eventService;

    @Override
    public void addLike(long userId, long filmId) throws UserNotFound, FilmNotFound {
        try {
            String sql = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES ( ?, ? )";
            jdbcTemplate.update(sql, userId, filmId);
        } catch (DataAccessException e) {
            throw e;
        }
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) throws FilmNotFound, UserNotFound {
        try {
            String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
            jdbcTemplate.update(sql, filmId, userId);
        }catch (EmptyResultDataAccessException e) {
            throw new FilmNotFound("");
        }
    }

    @Override
    public Integer getLikesCount(long id) throws UserNotFound {
        String sql = "SELECT COUNT(*) FROM LIKES WHERE FILM_ID = ?";
        Integer likes = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return likes;
    }

    @Override
    public Integer getUsefulCount(long reviewId) {
        String sqlLike = "SELECT COUNT(*) FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND ISLIKE = ?";
        Integer likes = jdbcTemplate.queryForObject(sqlLike, Integer.class, reviewId, true);

        String sqlDislike = "SELECT COUNT(*) FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND ISLIKE = ?";
        Integer dislikes = jdbcTemplate.queryForObject(sqlDislike, Integer.class, reviewId, false);
        return likes - dislikes;
    }

}
