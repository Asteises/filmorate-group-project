package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.MpaNotFound;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Data
public class MpaDbStorage implements MpaStorage {

    public final JdbcTemplate jdbcTemplate;

    public Mpa getMpaById(int id) throws MpaNotFound {
        try {
            String sql = "SELECT * FROM MPA WHERE ID = ?";
            return jdbcTemplate.queryForObject(sql, new MpaRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFound("");
        }
    }

    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }

}
