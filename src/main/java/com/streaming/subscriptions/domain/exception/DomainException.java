package com.streaming.subscriptions.domain.exception;

/**
 * Base for business-rule failures that map to HTTP responses in the REST adapter.
 * Status codes follow HTTP semantics (4xx client, 5xx server) without coupling the domain to Spring.
 */
public abstract class DomainException extends RuntimeException {

    private final int httpStatusCode;

    protected DomainException(int httpStatusCode, String message) {
        super(message);
        if (httpStatusCode < 100 || httpStatusCode > 599) {
            throw new IllegalArgumentException("httpStatusCode must be between 100 and 599");
        }
        this.httpStatusCode = httpStatusCode;
    }

    protected DomainException(int httpStatusCode, String message, Throwable cause) {
        super(message, cause);
        if (httpStatusCode < 100 || httpStatusCode > 599) {
            throw new IllegalArgumentException("httpStatusCode must be between 100 and 599");
        }
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public boolean isClientError() {
        return httpStatusCode >= 400 && httpStatusCode < 500;
    }

    public boolean isServerError() {
        return httpStatusCode >= 500;
    }
}
