package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final RuntimeException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExists(final RuntimeException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({ValidationException.class,
            NotAvailableException.class,
            NotValidDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final RuntimeException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleThrowable(final ConstraintViolationException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessageTemplate)
                        .findFirst().orElse("No message")
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleThrowable(final RuntimeException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }
}
