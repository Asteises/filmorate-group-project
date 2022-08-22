package ru.yandex.practicum.filmorate.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Data
@RequiredArgsConstructor
public class DirectorRowMapper implements RowMapper<Director> {

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("ID"));
        director.setName(rs.getString("NAME"));
        return director;
    }

}
