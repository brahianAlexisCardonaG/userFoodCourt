package com.project.usersFoodCourt.application.handler;

import com.project.usersFoodCourt.application.dto.request.UserAuthenticateRequestDto;
import com.project.usersFoodCourt.application.dto.request.UserRegisterRequestDto;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;

public interface IUserHandler {
    AuthenticationResponse registerUser(UserRegisterRequestDto userRegisterRequestDto);
    AuthenticationResponse authenticateUser(UserAuthenticateRequestDto userAuthenticateRequestDto);
}