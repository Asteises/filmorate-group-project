package ru.yandex.practicum.filmorate.exeption;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }

}
