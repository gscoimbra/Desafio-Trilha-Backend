package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.dto.NotificationRequestDto;
import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class NotificationController {

    private final ReceiveNotificationUseCase receiveNotificationUseCase;

    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@Valid @RequestBody NotificationRequestDto request) {
        receiveNotificationUseCase.execute(request.toDomain());
        return ResponseEntity.accepted().build();
    }
}
