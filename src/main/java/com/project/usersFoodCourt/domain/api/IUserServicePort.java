package com.project.usersFoodCourt.domain.api;

import com.project.usersFoodCourt.application.dto.response.UserRoleResponseDto;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import com.project.usersFoodCourt.domain.model.UserModel;

public interface IUserServicePort {
    AuthenticationResponse registerUserWithRoleValidation(UserModel userModel, String currentUserRole);
    AuthenticationResponse authenticateUser(UserModel userModel);
    UserModel getUserById(Long userId);
}
