package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.error.RestExceptionHandler;
import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(RestExceptionHandler.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiveNotificationUseCase receiveNotificationUseCase;

    @Test
    void receiveNotification_whenValidPayload_shouldReturn202() throws Exception {
        String body = """
                {"subscriptionId": 1, "type": "SUBSCRIPTION_PURCHASED"}
                """;

        mockMvc.perform(post("/api/subscriptions/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isAccepted());

        verify(receiveNotificationUseCase).execute(any());
    }

    @Test
    void receiveNotification_whenInvalidType_shouldReturn400() throws Exception {
        String body = """
                {"subscriptionId": 1, "type": "INVALID_TYPE"}
                """;

        mockMvc.perform(post("/api/subscriptions/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void receiveNotification_whenMissingSubscriptionId_shouldReturn400() throws Exception {
        String body = """
                {"type": "SUBSCRIPTION_PURCHASED"}
                """;

        mockMvc.perform(post("/api/subscriptions/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void receiveNotification_whenMissingType_shouldReturn400() throws Exception {
        String body = """
                {"subscriptionId": 1}
                """;

        mockMvc.perform(post("/api/subscriptions/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
