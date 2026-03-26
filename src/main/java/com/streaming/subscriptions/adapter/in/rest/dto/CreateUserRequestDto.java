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
@Schema(description = "Request para criar um usuário com assinatura inicial (canceled)")
public class CreateUserRequestDto {

    @NotBlank(message = "fullName é obrigatório")
    @Size(max = 255)
    @Schema(description = "Nome completo do usuário", example = "Gabriel Coimbra", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
}
