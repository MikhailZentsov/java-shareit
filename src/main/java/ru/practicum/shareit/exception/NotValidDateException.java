package ru.practicum.shareit.exception;

public class NotValidDateException extends RuntimeException {
    public NotValidDateException(String message) {
        super(message);
    }
}
