package com.streaming.subscriptions.adapter.in.rest.error;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Standard error response")
public class ApiError {

    @Schema(description = "When the error occurred")
    private final Instant timestamp;

    @Schema(description = "HTTP status code")
    private final int status;

    @Schema(description = "HTTP status name")
    private final String error;

    @Schema(description = "Human-readable error message")
    private final String message;

    @Schema(description = "Request path that caused the error")
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
