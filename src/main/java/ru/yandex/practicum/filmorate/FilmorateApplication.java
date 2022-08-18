package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO - Описание проекта и подсказки.
/**
 * queryForObject - используется там, где возвращается всегда одно значение!
 * Самый быстрый и оптимальный вариант работы с запросами - через JOIN, лучше отказаться от подзапросов!
 * Все запросы к БД выполняются очень медленно, по-этому лучше использовать индексы для большой БД.
 */
    @SpringBootApplication
    public class FilmorateApplication {
        public static void main(String[] args) {
            SpringApplication.run(FilmorateApplication.class, args);
        }
    }
