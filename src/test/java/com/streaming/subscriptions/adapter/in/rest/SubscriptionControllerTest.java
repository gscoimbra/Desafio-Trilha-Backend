package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.error.RestExceptionHandler;
import com.streaming.subscriptions.application.port.in.GetSubscriptionUseCase;
import com.streaming.subscriptions.domain.model.SubscriptionView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@Import(RestExceptionHandler.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetSubscriptionUseCase getSubscriptionUseCase;

    private static final SubscriptionView DEMO_VIEW = new SubscriptionView(
            1L, 1L, "Demo User", "ativa",
            Instant.parse("2025-01-01T12:00:00Z"),
            Instant.parse("2025-02-16T12:00:00Z")
    );

    @Test
    void getById_whenFound_shouldReturn200WithBody() throws Exception {
        when(getSubscriptionUseCase.getById(1L)).thenReturn(Optional.of(DEMO_VIEW));

        mockMvc.perform(get("/api/subscriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("Demo User"))
                .andExpect(jsonPath("$.statusName").value("ativa"));
    }

    @Test
    void getById_whenNotFound_shouldReturn404() throws Exception {
        when(getSubscriptionUseCase.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/subscriptions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_whenNoFilter_shouldReturnAll() throws Exception {
        when(getSubscriptionUseCase.listAll()).thenReturn(List.of(DEMO_VIEW));

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statusName").value("ativa"));
    }

    @Test
    void list_whenUserIdFilter_shouldReturnFiltered() throws Exception {
        when(getSubscriptionUseCase.listByUserId(1L)).thenReturn(List.of(DEMO_VIEW));

        mockMvc.perform(get("/api/subscriptions").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value(1));
    }
}
