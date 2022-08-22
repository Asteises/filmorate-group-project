package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    /**
     * Получаем Mpa по id
     */
    @GetMapping("{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return mpaService.getMpaById(id);
    }

    /**
     * Получаем все Mpa
     */
    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

}
