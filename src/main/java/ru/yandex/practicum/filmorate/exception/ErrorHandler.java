package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleHappinessOverflow(final NotFoundException e) {
        return new ExceptionResponse(
                "Объект не найден!",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIncorrectParameter(final ValidationException e) {
        return new ExceptionResponse(
                "Ошибка валидации.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleIncorrectParameter(final RuntimeException e) {
        return new ExceptionResponse(
                "Возникло исключение.",
                e.getMessage()
        );
    }
}