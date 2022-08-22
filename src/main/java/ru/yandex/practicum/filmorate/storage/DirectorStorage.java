package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFound;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Repository
public interface DirectorStorage {

    Director addDirector(Director director);

    List<Director> getAllDirectors();

    Director getDirectorById(int id) throws DirectorNotFound;

    Director updateDirector(Director director) throws DirectorNotFound;

    void deleteDirectorById(int id) throws DirectorNotFound;

    List<Director> getDirectorsByFilmId(long filmId) throws FilmNotFound;

}
