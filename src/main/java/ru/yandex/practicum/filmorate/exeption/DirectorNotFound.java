package ru.yandex.practicum.filmorate.exeption;

public class DirectorNotFound extends RuntimeException {

    public DirectorNotFound(String message) {
        super(message);
    }

}
