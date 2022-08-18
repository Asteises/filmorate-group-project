package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa getMpaById(int mpaId) {
        return mpaStorage.getMpaById(mpaId);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

}
