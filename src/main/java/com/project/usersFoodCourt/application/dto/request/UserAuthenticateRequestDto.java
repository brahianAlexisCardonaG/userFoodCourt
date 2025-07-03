package com.project.usersFoodCourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAuthenticateRequestDto {
    @NotBlank(message = "Field email cannot be empty or null.")
    private String email;

    @NotBlank(message = "Field password cannot be empty or null.")
    private String password;
}
