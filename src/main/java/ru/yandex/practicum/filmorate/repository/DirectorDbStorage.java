package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFound;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director addDirector(Director director) {
        String sql = "INSERT INTO DIRECTORS (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return director;
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sql, new DirectorRowMapper());
    }

    @Override
    public Director getDirectorById(int id) throws DirectorNotFound {
        try {
            String sql = "SELECT * FROM DIRECTORS D WHERE D.ID = ?";
            return jdbcTemplate.queryForObject(sql, new DirectorRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DirectorNotFound("");
        }
    }

    @Override
    public Director updateDirector(Director director) throws DirectorNotFound {
        try {
            getDirectorById(director.getId());
            String sql = "UPDATE DIRECTORS SET NAME = ? WHERE ID = ?";
            jdbcTemplate.update(sql, director.getName(), director.getId());
            return director;
        } catch (EmptyResultDataAccessException e) {
            throw new DirectorNotFound("");
        }

    }

    @Override
    public void deleteDirectorById(int id) throws DirectorNotFound {
        String sql = "DELETE FROM DIRECTORS WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Director> getDirectorsByFilmId(long id) {
        String sql = "SELECT * FROM DIRECTORS WHERE ID IN " +
                "(SELECT DIRECTOR_ID FROM FILMS_DIRECTORS WHERE FILM_ID = ?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Director.class), id);
    }

}
