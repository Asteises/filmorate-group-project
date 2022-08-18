package ru.yandex.practicum.filmorate.exeption;

public class GenreNotFound extends RuntimeException {

    public GenreNotFound(String message) {
        super(message);
    }

}
