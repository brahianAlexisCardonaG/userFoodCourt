package com.project.usersFoodCourt.domain.api;

import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import com.project.usersFoodCourt.domain.model.UserModel;

public interface IUserServicePort {
    AuthenticationResponse registerUser(UserModel userModel);
    AuthenticationResponse authenticateUser(UserModel userModel);
}
