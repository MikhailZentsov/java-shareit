package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void testHandleNotFound() {
        NotFoundException notFoundException = new NotFoundException("Не найден");
        Map<String, String> result = errorHandler.handleNotFound(notFoundException);
        assertEquals("Не найден", result.get("error"));
    }

    @Test
    void testHandleConflict() {
        AlreadyExistsException alreadyExistsException = new AlreadyExistsException("Уже существует");
        Map<String, String> result = errorHandler.handleAlreadyExists(alreadyExistsException);
        assertEquals("Уже существует", result.get("error"));
    }

    @Test
    void testHandleInternalError() {
        Throwable Throwable = new Throwable("Ошибка приложения");
        Map<String, String> result = errorHandler.handleThrowable(Throwable);
        assertEquals("Ошибка приложения", result.get("error"));
    }
}