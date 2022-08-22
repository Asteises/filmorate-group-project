package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Получаем Review по id
     */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    /**
     * Получаем Reviews
     */
    @GetMapping
    public Collection<Review> getReviews(@RequestParam(defaultValue = "0") int filmId,
                                         @RequestParam(defaultValue = "0") int count) {
        return reviewService.getReviews(filmId, count);
    }

    /**
     * Добавляем новый Review
     */
    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     * Изменяем существующий Review
     */
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    /**
     * Пользователь ставит лайк Review
     */
    @PutMapping("/{id}/like/{userId}")
    public void putReviewLike(@PathVariable int id,@PathVariable int userId) {
        reviewService.putReviewLike(id, userId);
    }

    /**
     * Пользователь ставит дизлайк Review
     */
    @PutMapping("/{id}/dislike/{userId}")
    public void putReviewDislike(@PathVariable int id,@PathVariable int userId) {
        reviewService.putReviewDislike(id, userId);
    }

    /**
     * Пользователь удаляет лайк Review
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteReviewLike(@PathVariable int id,@PathVariable int userId) {
        reviewService.deleteReviewLike(id, userId);
    }

    /**
     * Пользователь удаляет дизлайк Review
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteReviewDislike(@PathVariable int id,@PathVariable int userId) {
        reviewService.deleteReviewDislike(id, userId);
    }

    /**
     * Удаление Review по id
     */
    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable int id) {
        reviewService.deleteReviewById(id);
    }

}
