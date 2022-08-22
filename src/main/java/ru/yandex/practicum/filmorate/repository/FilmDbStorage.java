package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFound;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final LikesDbStorage likesDbStorage;
    private final DirectorDbStorage directorDbStorage;

    @Override
    public Film addFilm(Film film) throws FilmNotFound {
        film.setRate(0);
        String sql = "INSERT INTO FILMS (" +
                "NAME, " +
                "MPA_ID, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
            ps.setString(1, film.getName());
            ps.setInt(2, film.getMpa().getId());
            ps.setString(3, film.getDescription());
            LocalDate realeaseDate = film.getReleaseDate();
            if (realeaseDate == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(realeaseDate));
            }
            ps.setInt(5, film.getDuration());
            return ps;

        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        setFilmGenres(film.getId(), film.getGenres());
        setFilmDirectors(film.getId(), film.getDirectors());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql1 = "SELECT * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID";
        return jdbcTemplate.query(sql1, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage));
    }

    @Override
    public Film getFilmById(long id) throws FilmNotFound {
        try {
            String sql = "SELECT * FROM FILMS " +
                    "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                    "WHERE FILMS.ID = ?";
            return jdbcTemplate.queryForObject(sql, new FilmRowMapper(genreDbStorage,
                    mpaDbStorage, likesDbStorage, directorDbStorage), id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFound("");
        }
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFound {
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        film.setDirectors(film.getDirectors().stream().distinct().collect(Collectors.toList()));
        getFilmById(film.getId());
        String sql = "UPDATE FILMS SET " +
                "NAME = ?, " +
                "MPA_ID = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        setFilmGenres(film.getId(), film.getGenres());
        setFilmDirectors(film.getId(), film.getDirectors());
        return film;
    }

    @Override
    public void deleteFilm(long filmId) throws FilmNotFound {
        try {
            String sql = "DELETE FROM FILMS WHERE ID = ?";
            String sqlDirector = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
            String sqlGenre = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            String sqlLike = "DELETE FROM LIKES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlGenre, filmId);
            jdbcTemplate.update(sqlLike, filmId);
            jdbcTemplate.update(sqlDirector, filmId);
            jdbcTemplate.update(sql, filmId);
        } catch (IllegalArgumentException e) {
            throw new FilmNotFound("");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT TOP ? * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                "LEFT JOIN LIKES L ON FILMS.ID = L.FILM_ID " +
                "GROUP BY FILMS.ID, " +
                "L.USER_ID " +
                "ORDER BY COUNT(USER_ID) DESC";
        if (count != 0) {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                    likesDbStorage, directorDbStorage), count);
        } else {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), 10);
        }
    }

    @Override
    public List<Film> getPopularByGenre(int genreId) {
        String sql = "SELECT TOP ? * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                "LEFT JOIN LIKES L ON FILMS.ID = L.FILM_ID LEFT JOIN FILMS_GENRES AS FG ON FILMS.ID = FG.FILM_ID " +
                "WHERE FG.GENRE_ID = ? " +
                "GROUP BY FILMS.ID, " +
                "L.USER_ID " +
                "ORDER BY COUNT(USER_ID) DESC";
        return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), 10, genreId);
    }

    @Override
    public List<Film> getPopularFilmsByYear(String year) {
        String sql = "SELECT TOP ? * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                "LEFT JOIN LIKES L ON FILMS.ID = L.FILM_ID WHERE  (RELEASE_DATE BETWEEN ? AND ?) " +
                "GROUP BY FILMS.ID, " +
                "L.USER_ID " +
                "ORDER BY COUNT(USER_ID) DESC";
        String firstBoundOfDate = Integer.parseInt(year) - 1 + "-12-31";
        String secondBoundOfDate = Integer.parseInt(year) + 1 + "-01-01";
        return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage,
                directorDbStorage), 10, firstBoundOfDate, secondBoundOfDate);

    }

    @Override
    public List<Film> getPopularFilmsByGenreAndYear(int count, int genreId, String year) {
        String sql = "SELECT TOP ? * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                "LEFT JOIN LIKES L ON FILMS.ID = L.FILM_ID LEFT JOIN FILMS_GENRES AS FG ON FILMS.ID = FG.FILM_ID " +
                "WHERE  (RELEASE_DATE BETWEEN ? AND ?) AND FG.GENRE_ID = ?" +
                "GROUP BY FILMS.ID, " +
                "L.USER_ID " +
                "ORDER BY COUNT(USER_ID) DESC";
        String firstBoundOfDate = Integer.parseInt(year) - 1 + "-12-31";
        String secondBoundOfDate = Integer.parseInt(year) + 1 + "-01-01";
        if (count != 0) {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                    likesDbStorage, directorDbStorage), count, firstBoundOfDate, secondBoundOfDate, genreId);
        } else {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                    likesDbStorage, directorDbStorage), 10, firstBoundOfDate, secondBoundOfDate, genreId);
        }
    }

    @Override
    public Collection<Film> getCommonFilms(int userId, int friendId) {
        String sql = "SELECT * FROM FILMS AS F JOIN MPA ON F.MPA_ID=MPA.ID LEFT JOIN LIKES L " +
                "ON F.ID = L.FILM_ID LEFT JOIN FILMS_GENRES AS FG ON F.ID = FG.FILM_ID WHERE L.USER_ID = ? " +
                "AND F.ID IN (SELECT LIKES.FILM_ID FROM LIKES WHERE USER_ID = ?) " +
                "GROUP BY F.ID, " +
                "L.USER_ID ORDER BY COUNT(USER_ID) DESC LIMIT ?";

        return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                likesDbStorage, directorDbStorage),  friendId, userId, 10);
    }

    @Override
    public void setFilmGenres(long filmId, List<Genre> genres) throws FilmNotFound {
        String sqlCheck = "SELECT COUNT(*) FROM FILMS_GENRES WHERE FILM_ID = ?";
        Integer check = jdbcTemplate.queryForObject(sqlCheck, Integer.class, filmId);
        if (check == 0) {
            for (Genre genre : genres) {
                String sqlInsert = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlInsert, filmId, genre.getId());
            }
        } else {
            String sqlDelete = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlDelete, filmId);
            for (Genre genre : genres) {
                String sqlMerge = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlMerge, filmId, genre.getId());
            }
        }
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) throws FilmNotFound {
        String sqlSelect = "SELECT * FROM FILMS_GENRES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlSelect, new GenreRowMapper(), filmId);
    }

    @Override
    public void setFilmDirectors(long filmId, List<Director> directors) throws FilmNotFound {
        String sqlCheck = "SELECT COUNT(*) FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
        Integer check = jdbcTemplate.queryForObject(sqlCheck, Integer.class, filmId);
        if (check == 0) {
            for (Director director : directors) {
                String sqlInsert = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlInsert, filmId, director.getId());
            }
        } else {
            String sqlDelete = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlDelete, filmId);
            for (Director director : directors) {
                String sqlMerge = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlMerge, filmId, director.getId());
            }
        }
    }

    @Override
    public List<Film> getAllFilmsByDirector(int directorId, String sortBy) throws DirectorNotFound {
        try {
            directorDbStorage.getDirectorById(directorId);
            if (sortBy.equals("year")) {
                String sql = "SELECT * FROM FILMS " +
                        "RIGHT JOIN (SELECT FILM_ID FROM FILMS_DIRECTORS WHERE DIRECTOR_ID = ?) FD ON FILMS.ID=FD.FILM_ID " +
                        "JOIN MPA ON FILMS.MPA_ID=MPA.ID ORDER BY RELEASE_DATE";
                return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), directorId);
            } else if (sortBy.equals("likes")) {
                String sql = "SELECT * FROM FILMS " +
                        "JOIN ((SELECT FILM_ID FROM FILMS_DIRECTORS WHERE DIRECTOR_ID = ?)) FD ON FILMS.ID=FD.FILM_ID " +
                        "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                        "LEFT JOIN LIKES l ON FILMS.ID = l.FILM_ID " +
                        "GROUP BY FILMS.ID " +
                        "ORDER BY COUNT(USER_ID) DESC";
                return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                        likesDbStorage, directorDbStorage), directorId);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DirectorNotFound("");
        }
        return null;
    }

    @Override
    public Collection<Film> getSearchFilmsByTittleAndDirector(String query) {
        String searchFilmsByDirectorAndTittle = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILMS_DIRECTORS AS FD ON " +
                "F.ID = FD.FILM_ID LEFT JOIN DIRECTORS AS D ON FD.DIRECTOR_ID = D.ID " +
                "LEFT JOIN LIKES AS L ON L.FILM_ID = F.ID LEFT JOIN MPA AS M ON M.ID = F.MPA_ID WHERE " +
                "F.NAME ~* ? OR D.NAME ~* ? GROUP BY F.ID ORDER BY COUNT(USER_ID) ";

        List<Film> films = jdbcTemplate.query(searchFilmsByDirectorAndTittle,
                new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), query, query);

        Comparator<Film> filmComparator = Comparator.comparing(Film::getRate).reversed();
        films.sort(filmComparator);
        return films;
    }

    @Override
    public Collection<Film> getSearchFilmsByTittle(String query) {
        String searchFilmsByDirectorAndTittle = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILMS_DIRECTORS AS FD ON " +
                "F.ID = FD.FILM_ID LEFT JOIN DIRECTORS AS D ON FD.DIRECTOR_ID = D.ID " +
                "LEFT JOIN LIKES AS L ON L.FILM_ID = F.ID LEFT JOIN MPA AS M ON M.ID = F.MPA_ID WHERE " +
                "F.NAME ~* ? GROUP BY F.ID ORDER BY COUNT(USER_ID) ";

        List<Film> films = jdbcTemplate.query(searchFilmsByDirectorAndTittle,
                new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), query);

        Comparator<Film> filmComparator = Comparator.comparing(Film::getRate).reversed();
        films.sort(filmComparator);
        return films;
    }

    @Override
    public Collection<Film> getSearchFilmsByDirector(String query) {
        String searchFilmsByDirectorAndTittle = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILMS_DIRECTORS AS FD ON " +
                "F.ID = FD.FILM_ID LEFT JOIN DIRECTORS AS D ON FD.DIRECTOR_ID = D.ID " +
                "LEFT JOIN LIKES AS L ON L.FILM_ID = F.ID LEFT JOIN MPA AS M ON M.ID = F.MPA_ID WHERE " +
                " D.NAME ~* ? GROUP BY F.ID ORDER BY COUNT(USER_ID) ";

        List<Film> films = jdbcTemplate.query(searchFilmsByDirectorAndTittle,
                new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage, directorDbStorage), query);

        Comparator<Film> filmComparator = Comparator.comparing(Film::getRate).reversed();
        films.sort(filmComparator);
        return films;
    }

}