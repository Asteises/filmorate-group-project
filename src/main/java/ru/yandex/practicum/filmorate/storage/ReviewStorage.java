package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

@Repository
public interface ReviewStorage {
    Review addReview(Review review);

    Review updateReview(Review review);

    void putReviewLike(int reviewId, int userId);

    void putReviewDislike(int reviewId, int userId);

    void deleteReviewLike(int reviewId, int userId);

    void deleteReviewDislike(int reviewId, int userId);

    Review getReviewById(int reviewId);

    void deleteReviewById(int reviewId);

    Collection<Review> getReviews();

    Collection<Review> getReviewsByFilmId(int filmId);
}
