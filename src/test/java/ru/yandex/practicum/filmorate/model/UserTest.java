package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class UserTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void validateUserEmailNotBlankTest() {
        User user = new User();
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно равняться null")));
    }

    @Test
    public void validateUserEmailTest() {
        User user = new User();
        user.setEmail("useremail.test");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно иметь формат адреса электронной почты")));
    }

    @Test
    public void validateUserLoginTest() {
        User user = new User();
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно соответствовать \"\\S+\"")));
    }

    @Test
    public void validateUserBirthdayTest() {
        User user = new User();
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно содержать прошедшую дату или сегодняшнее число")));
    }
}
