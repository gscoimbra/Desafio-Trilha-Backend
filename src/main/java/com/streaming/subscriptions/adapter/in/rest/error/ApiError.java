package com.streaming.subscriptions.adapter.in.rest.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * Standard API error body for REST responses (aligned with common problem-detail style fields).
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiError {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public static ApiError of(HttpStatus httpStatus, String message, String path) {
        return new ApiError(
                Instant.now(),
                httpStatus.value(),
                httpStatus.name(),
                message,
                path != null ? path : ""
        );
    }

    public static ApiError of(int statusCode, String message, String path) {
        HttpStatus resolved = HttpStatus.resolve(statusCode);
        String errorName = resolved != null ? resolved.name() : "HTTP_" + statusCode;
        return new ApiError(Instant.now(), statusCode, errorName, message, path != null ? path : "");
    }
}
