package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.dto.CreateUserRequestDto;
import com.streaming.subscriptions.adapter.in.rest.error.ApiError;
import com.streaming.subscriptions.application.port.in.CreateUserUseCase;
import com.streaming.subscriptions.domain.model.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User creation and management")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @Operation(
            summary = "Create user",
            description = "Creates a user with an initial subscription in cancelada status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<UserView> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        UserView view = createUserUseCase.execute(request.getFullName().trim());
        return ResponseEntity.status(201).body(view);
    }
}
