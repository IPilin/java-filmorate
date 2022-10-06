package ru.yandex.practicum.filmorate.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(final String message) {
        super(message);
    }
}
