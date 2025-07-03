package com.project.usersFoodCourt.domain.usecase;

import com.project.usersFoodCourt.domain.api.IUserServicePort;
import com.project.usersFoodCourt.domain.exception.BusinessException;
import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.domain.model.util.DomainUserDetails;
import com.project.usersFoodCourt.domain.spi.IRolePersistencePort;
import com.project.usersFoodCourt.domain.spi.IUserPersistencePort;
import com.project.usersFoodCourt.infrastructure.configuration.JwtService;
import com.project.usersFoodCourt.utils.ErrorCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final PasswordEncoder passwordEncoder;
    private final IUserPersistencePort userPersistencePort;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IRolePersistencePort rolePersistencePort;

    @Override
    public AuthenticationResponse registerUser(UserModel userModel) {

        Optional<RoleModel> roleModel = rolePersistencePort.findByRoleId(userModel.getRole().getId());
        if(roleModel.isEmpty()){
            throw new BusinessException(ErrorCatalog.ROLE_ID_NOT_FOUND);
        }

        if (userModel.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new BusinessException(ErrorCatalog.USER_UNDERAGE);
        }
        
        userModel.setRole(roleModel.get());
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userPersistencePort.save(userModel);

        DomainUserDetails userDetails = new DomainUserDetails(userModel);
        var jwtToken = jwtService.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtToken);
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse authenticateUser(UserModel userModel) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userModel.getEmail(),
                        userModel.getPassword()
                )
        );
        var userFound = userPersistencePort.findByEmail(userModel.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCatalog.USER_NOT_FOUND));

        DomainUserDetails userDetails = new DomainUserDetails(userFound);
        var jwtToken = jwtService.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtToken);
        return authenticationResponse;
    }
}
