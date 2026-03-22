package com.streaming.subscriptions;

import com.streaming.subscriptions.application.port.in.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CreateUserIntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Test
    void createUser_shouldCreateWithSubscriptionCancelada() {
        var view = createUserUseCase.execute("João Santos");

        assertThat(view.id()).isNotNull();
        assertThat(view.fullName()).isEqualTo("João Santos");
        assertThat(view.createdAt()).isNotNull();
        assertThat(view.subscriptionId()).isNotNull();
        assertThat(view.subscriptionStatus()).isEqualTo("cancelada");
    }
}
