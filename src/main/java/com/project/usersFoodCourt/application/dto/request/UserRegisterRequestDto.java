package com.project.usersFoodCourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterRequestDto {
    private Long id;

    @NotBlank(message = "Field firstname cannot be empty or null.")
    private String firstName;

    @NotBlank(message = "Field lastname cannot be empty or null.")
    private String lastName;

    @NotBlank(message = "Field documentNumber cannot be empty or null.")
    @Pattern(regexp = "^[0-9]+$", message = "Document number must be numeric only.")
    private String documentNumber;

    @NotBlank(message = "Field phone cannot be empty or null.")
    @Pattern(regexp = "^\\+?[0-9]{1,13}$", message = "Phone must contain maximum 13 characters and can include + symbol.")
    private String phone;

    @NotNull(message = "Field birthDate cannot be empty or null.")
    private LocalDate birthDate;

    @NotBlank(message = "Field email cannot be empty or null.")
    @Email(message = "Email format is invalid.")
    private String email;

    @NotBlank(message = "Field password cannot be empty or null.")
    private String password;

    @NotNull(message = "Field roleId cannot be empty or null.")
    private Long roleId;
}