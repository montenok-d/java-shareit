package ru.practicum.shareit.error;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
            super(message);
        }
}
