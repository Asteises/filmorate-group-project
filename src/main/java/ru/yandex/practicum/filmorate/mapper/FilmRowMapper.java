package ru.yandex.practicum.filmorate.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.DirectorDbStorage;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.LikesDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final LikesDbStorage likesDbStorage;
    private final DirectorDbStorage directorDbStorage;

    /**
     * Получаем Film из строки базы данных
     */
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa();
        film.setId(rs.getLong("ID"));
        film.setName(rs.getString("NAME"));
        mpa.setId(rs.getInt("MPA.ID"));
        mpa.setName(rs.getString("MPA.NAME"));
        film.setMpa(mpa);
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setRate(likesDbStorage.getLikesCount(rs.getLong("ID")));
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        film.setDirectors(directorDbStorage.getDirectorsByFilmId(film.getId()));
        return film;
    }
}
