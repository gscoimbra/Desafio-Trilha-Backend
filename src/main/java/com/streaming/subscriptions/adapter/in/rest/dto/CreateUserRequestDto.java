package com.streaming.subscriptions.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a user with initial subscription (cancelada)")
public class CreateUserRequestDto {

    @NotBlank(message = "fullName is required")
    @Size(max = 255)
    @Schema(description = "User full name", example = "Maria Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
}
