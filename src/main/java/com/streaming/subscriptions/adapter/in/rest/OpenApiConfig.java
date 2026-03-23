package com.streaming.subscriptions.adapter.in.rest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subscription Service API")
                        .description("API for receiving and processing streaming subscription notifications. " +
                                "Notifications are enqueued in Kafka and processed asynchronously.")
                        .version("1.0.0"));
    }
}
