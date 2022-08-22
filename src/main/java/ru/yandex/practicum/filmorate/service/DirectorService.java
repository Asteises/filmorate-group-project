package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFound;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DirectorService {

    private DirectorStorage directorStorage;

    public Director addDirector(Director director) throws DirectorNotFound {
        return directorStorage.addDirector(director);
    }

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(int id) throws DirectorNotFound {
        return directorStorage.getDirectorById(id);
    }

    public Director updateDirector(Director director) throws DirectorNotFound {
        return directorStorage.updateDirector(director);
    }

    public void deleteDirectorById(int id) throws DirectorNotFound {
        directorStorage.deleteDirectorById(id);
    }

}
