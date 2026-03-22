package com.streaming.subscriptions.domain.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(Long userId) {
        super(404, "User not found: " + userId);
    }
}
