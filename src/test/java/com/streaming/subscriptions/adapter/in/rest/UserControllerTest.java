package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.error.RestExceptionHandler;
import com.streaming.subscriptions.application.port.in.CreateUserUseCase;
import com.streaming.subscriptions.application.port.in.GetUserUseCase;
import com.streaming.subscriptions.domain.model.UserView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(RestExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetUserUseCase getUserUseCase;

    private static final UserView DEMO_VIEW = new UserView(
            1L, "Demo User", Instant.parse("2025-01-01T12:00:00Z"), 1L, "ativa"
    );

    @Test
    void createUser_whenValid_shouldReturn201() throws Exception {
        UserView view = new UserView(1L, "Maria Silva", Instant.now(), 1L, "cancelada");
        when(createUserUseCase.execute("Maria Silva")).thenReturn(view);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"Maria Silva\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Maria Silva"))
                .andExpect(jsonPath("$.subscriptionStatus").value("cancelada"));
    }

    @Test
    void createUser_whenEmptyFullName_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenMissingFullName_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_whenFound_shouldReturn200() throws Exception {
        when(getUserUseCase.getById(1L)).thenReturn(Optional.of(DEMO_VIEW));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Demo User"))
                .andExpect(jsonPath("$.subscriptionStatus").value("ativa"));
    }

    @Test
    void getById_whenNotFound_shouldReturn404() throws Exception {
        when(getUserUseCase.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_shouldReturnAllUsers() throws Exception {
        when(getUserUseCase.listAll()).thenReturn(List.of(DEMO_VIEW));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subscriptionStatus").value("ativa"));
    }
}
