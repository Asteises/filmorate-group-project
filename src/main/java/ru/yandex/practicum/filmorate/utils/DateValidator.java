package ru.yandex.practicum.filmorate.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<DateAfter, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate != null) {
            return localDate.isAfter(LocalDate.of(1895, 12, 28));
        }
        return true;
    }
}
