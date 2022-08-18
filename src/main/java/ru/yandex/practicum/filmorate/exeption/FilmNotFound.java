package ru.yandex.practicum.filmorate.exeption;

public class FilmNotFound extends RuntimeException {

    public FilmNotFound(String message) {
        super(message);
    }

}
