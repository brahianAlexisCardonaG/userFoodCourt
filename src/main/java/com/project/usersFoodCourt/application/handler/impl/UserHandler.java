package com.project.usersFoodCourt.application.handler.impl;

import com.project.usersFoodCourt.application.dto.request.UserAuthenticateRequestDto;
import com.project.usersFoodCourt.application.dto.request.UserRegisterRequestDto;
import com.project.usersFoodCourt.application.handler.IUserHandler;
import com.project.usersFoodCourt.application.mapper.user.IUserRequestMapper;
import com.project.usersFoodCourt.domain.api.IUserServicePort;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort iUserServicePort;
    private final IUserRequestMapper iUserRequestMapper;

    @Override
    public AuthenticationResponse registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        return iUserServicePort.registerUser(iUserRequestMapper.toUserRegisterModel(userRegisterRequestDto));
    }

    @Override
    public AuthenticationResponse authenticateUser(UserAuthenticateRequestDto userAuthenticateRequestDto) {
        return iUserServicePort.authenticateUser(iUserRequestMapper.toUserAuthenticateModel(userAuthenticateRequestDto));
    }
}
