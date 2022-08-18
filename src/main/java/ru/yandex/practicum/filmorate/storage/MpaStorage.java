package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public interface MpaStorage {

    Mpa getMpaById(int id);

    List<Mpa> getAllMpa();

}
