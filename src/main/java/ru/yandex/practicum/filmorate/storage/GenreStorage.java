package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public interface GenreStorage {

    Genre getGenreById(long id);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(long id);

}
