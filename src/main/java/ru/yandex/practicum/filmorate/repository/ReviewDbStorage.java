package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.ReviewNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.Collection;

@Data
@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    private final LikesDbStorage likesDbStorage;
    private final EventService eventService;

    @Override
    public Review addReview(Review review) {
        review.setUseful(0);
        SqlRowSet isExistFilm = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE ID = ?", review.getFilmId());
        SqlRowSet isExistUser = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE ID = ?", review.getUserId());
        if (!isExistFilm.next()) {
            if (review.getFilmId() == 0) {
                throw new IllegalArgumentException("Поле filmId не должно быть пустым");
            }
            throw new FilmNotFound("Неверный id фильма");
        } else if (!isExistUser.next()) {
            if (review.getUserId() == 0) {
                throw new IllegalArgumentException("Поле userId не должно быть пустым");
            }
            throw new UserNotFound("Неверный id пользователя");
        } else {
            String sql = "INSERT INTO REVIEWS (" +
                    "CONTENT, " +
                    "ISPOSITIVE, " +
                    "USER_ID, " +
                    "FILM_ID) " +
                    "VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
                ps.setString(1, review.getContent());
                ps.setBoolean(2, review.getIsPositive());
                ps.setInt(3, review.getUserId());
                ps.setInt(4, review.getFilmId());
                return ps;

            }, keyHolder);
            review.setReviewId(keyHolder.getKey().longValue());

            String sqlReview = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ? ";
            Review review1 = jdbcTemplate.queryForObject(sqlReview, new ReviewRowMapper(likesDbStorage),
                                                                    review.getReviewId());
            eventService.addEvent(review1.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId());
        }
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE REVIEWS SET " +
                "CONTENT = ?, " +
                "ISPOSITIVE = ?" +
                "WHERE REVIEW_ID = ? ";
        jdbcTemplate.update(sql,review.getContent(), review.getIsPositive(), review.getReviewId());

        String sqlReview = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ? ";
        Review review1 = jdbcTemplate.queryForObject(sqlReview, new ReviewRowMapper(likesDbStorage), review.getReviewId());
        eventService.addEvent(review1.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        return review;
    }

    @Override
    public void putReviewLike(int reviewId, int userId) {
        SqlRowSet isUserPutDislike = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ? AND USER_ID = ? AND ISLIKE = ?", reviewId, userId, false);
        SqlRowSet isUserPutLike = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ? AND USER_ID = ? AND ISLIKE = ?", reviewId, userId, true);

        if (!isUserPutLike.next() && !isUserPutDislike.next()) {
            String sql = "INSERT INTO REVIEW_LIKES (" +
                    "REVIEW_ID, " +
                    "USER_ID, " +
                    "ISLIKE) " +
                    "VALUES (?, ?, ?)";

            jdbcTemplate.update(sql,reviewId, userId, true);
        }
    }

    @Override
    public void putReviewDislike(int reviewId, int userId) {
        SqlRowSet isUserPutDislike = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ? AND USER_ID = ? AND ISLIKE = ?", reviewId, userId, false);
        SqlRowSet isUserPutLike = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ? AND USER_ID = ? AND ISLIKE = ?", reviewId, userId, true);

        if (!isUserPutLike.next() && !isUserPutDislike.next()) {
            String sql = "INSERT INTO REVIEW_LIKES (" +
                    "REVIEW_ID, " +
                    "USER_ID, " +
                    "ISLIKE) " +
                    "VALUES (?, ?, ?)";

            jdbcTemplate.update(sql,reviewId, userId, false);
        }
    }

    @Override
    public void deleteReviewLike(int reviewId, int userId) {
        String deleteLike = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND " +
                "USER_ID = ? AND ISLIKE = ?";
        jdbcTemplate.update(deleteLike, reviewId, userId, true);
    }

    @Override
    public void deleteReviewDislike(int reviewId, int userId) {
        String deleteDislike = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND " +
                "USER_ID = ? AND ISLIKE = ?";
        jdbcTemplate.update(deleteDislike, reviewId, userId, true);
    }

    @Override
    public Review getReviewById(int reviewId) {
        try {
            String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ? ";
            return jdbcTemplate.queryForObject(sql, new ReviewRowMapper(likesDbStorage), reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFound("");
        }
    }

    @Override
    public void deleteReviewById(int reviewId) {
        try {
            String sqlReview = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ? ";
            Review review1 = jdbcTemplate.queryForObject(sqlReview, new ReviewRowMapper(likesDbStorage), reviewId);
            eventService.addEvent(review1.getUserId(), EventType.REVIEW, Operation.REMOVE, reviewId);

            String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
            String sqlLike = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ?";
            jdbcTemplate.update(sqlLike, reviewId);
            jdbcTemplate.update(sql, reviewId);
        } catch (IllegalArgumentException e) {
            throw new ReviewNotFound("");
        }
    }

    @Override
    public Collection<Review> getReviews() {
        String sql = "SELECT * FROM REVIEWS";
        return jdbcTemplate.query(sql, new ReviewRowMapper(likesDbStorage));
    }

    @Override
    public Collection<Review> getReviewsByFilmId(int filmId) {
        String sql = "SELECT * FROM REVIEWS WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, new ReviewRowMapper(likesDbStorage), filmId);
    }

}
