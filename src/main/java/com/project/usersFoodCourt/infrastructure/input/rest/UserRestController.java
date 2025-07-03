package com.project.usersFoodCourt.infrastructure.input.rest;

import com.project.usersFoodCourt.application.dto.request.UserAuthenticateRequestDto;
import com.project.usersFoodCourt.application.dto.request.UserRegisterRequestDto;
import com.project.usersFoodCourt.application.handler.IUserHandler;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserRestController {
    private final IUserHandler iUserHandler;

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user created", content = @Content),
            @ApiResponse(responseCode = "409", description = "user already exists", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.ok(iUserHandler.registerUser(userRegisterRequestDto));
    }

    @Operation(summary = "User Logged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user logged", content = @Content),
            @ApiResponse(responseCode = "409", description = "user not exists", content = @Content)
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody UserAuthenticateRequestDto userAuthenticateRequestDto
    ) {
        return ResponseEntity.ok(iUserHandler.authenticateUser(userAuthenticateRequestDto));
    }
}
