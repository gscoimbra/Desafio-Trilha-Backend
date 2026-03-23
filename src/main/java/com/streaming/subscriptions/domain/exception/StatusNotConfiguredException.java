package com.streaming.subscriptions.domain.exception;

/**
 * Required status row missing in the database (configuration / migration issue).
 */
public class StatusNotConfiguredException extends DomainException {

    public StatusNotConfiguredException(String statusName) {
        super(500, "Status not configured in database: " + statusName);
    }
}
