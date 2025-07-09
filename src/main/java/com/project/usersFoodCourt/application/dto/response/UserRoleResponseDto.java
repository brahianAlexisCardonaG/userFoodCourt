package com.project.usersFoodCourt.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private RoleResponse role;
}
