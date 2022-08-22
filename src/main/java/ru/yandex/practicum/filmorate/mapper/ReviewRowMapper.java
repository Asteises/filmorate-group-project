package ru.yandex.practicum.filmorate.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.LikesDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Data
@RequiredArgsConstructor
public class ReviewRowMapper implements RowMapper<Review> {
    private final LikesDbStorage likesDbStorage;

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getLong("REVIEW_ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setIsPositive(rs.getBoolean("ISPOSITIVE"));
        review.setUserId(rs.getInt("USER_ID"));
        review.setFilmId(rs.getInt("FILM_ID"));
        review.setUseful(likesDbStorage.getUsefulCount(review.getReviewId()));
        return review;
    }

}
