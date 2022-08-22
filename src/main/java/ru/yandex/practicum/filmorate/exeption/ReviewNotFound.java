package ru.yandex.practicum.filmorate.exeption;

public class ReviewNotFound extends RuntimeException{
    public ReviewNotFound(String message) {
        super(message);
    }

}
