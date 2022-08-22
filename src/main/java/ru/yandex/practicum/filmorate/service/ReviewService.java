package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public Review addReview(Review review) {
        return reviewStorage.addReview(review);
    }

    public Review getReviewById(int reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public void putReviewLike(int reviewId, int userId) {
        reviewStorage.putReviewLike(reviewId, userId);
    }

    public void putReviewDislike(int id, int userId) {
        reviewStorage.putReviewDislike(id, userId);
    }

    public void deleteReviewLike(int id, int userId) {
        reviewStorage.deleteReviewLike(id, userId);
    }

    public void deleteReviewDislike(int id, int userId) {
        reviewStorage.deleteReviewDislike(id, userId);
    }

    public void deleteReviewById(int reviewId) {
        reviewStorage.deleteReviewById(reviewId);
    }

    public Collection<Review> getReviews(int filmId, int count) {
        if (filmId == 0 && count == 0) {
            return reviewStorage.getReviews().stream().sorted(Comparator.comparing(Review::getUseful).reversed())
                    .collect(Collectors.toList());
        } else if (filmId == 0 && count != 0) {
            return reviewStorage.getReviews().stream().sorted(Comparator.comparing(Review::getUseful).reversed())
                    .limit(count).collect(Collectors.toList());
        } else if (filmId != 0 && count == 0) {
            return reviewStorage.getReviewsByFilmId(filmId).stream().sorted(Comparator.comparing(Review::getUseful)
                    .reversed()).limit(10).collect(Collectors.toCollection(ArrayList::new));
        } else {
            return reviewStorage.getReviewsByFilmId(filmId).stream().sorted(Comparator.comparing(Review::getUseful)
                    .reversed()).limit(count).collect(Collectors.toCollection(ArrayList::new));
        }
    }

}
