package com.project.usersFoodCourt.domain.usecase;

import com.project.usersFoodCourt.domain.exception.BusinessException;
import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import com.project.usersFoodCourt.domain.spi.IRolePersistencePort;
import com.project.usersFoodCourt.domain.spi.IUserPersistencePort;
import com.project.usersFoodCourt.infrastructure.configuration.JwtService;
import com.project.usersFoodCourt.utils.ErrorCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private IRolePersistencePort rolePersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private UserModel userModel;
    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("USER");

        userModel = new UserModel();
        userModel.setEmail("test@test.com");
        userModel.setPassword("password");
        userModel.setBirthDate(LocalDate.now().minusYears(20));
        userModel.setRole(roleModel);
    }

    @Test
    void registerUser_ShouldReturnAuthenticationResponse_WhenRoleExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthenticationResponse result = userUseCase.registerUser(userModel);

        assertNotNull(result);
        assertEquals("jwt-token", result.getAccessToken());
        verify(userPersistencePort).save(userModel);
        verify(rolePersistencePort).findByRoleId(1L);
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenRoleNotExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUser(userModel));

        assertEquals(ErrorCatalog.ROLE_ID_NOT_FOUND, exception.getErrorCatalog());
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenUserUnderage() {
        userModel.setBirthDate(LocalDate.now().minusYears(17));
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUser(userModel));

        assertEquals(ErrorCatalog.USER_UNDERAGE, exception.getErrorCatalog());
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void authenticateUser_ShouldReturnAuthenticationResponse_WhenUserExists() {
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.of(userModel));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthenticationResponse result = userUseCase.authenticateUser(userModel);

        assertNotNull(result);
        assertEquals("jwt-token", result.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticateUser_ShouldThrowBusinessException_WhenUserNotExists() {
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.authenticateUser(userModel));

        assertEquals(ErrorCatalog.USER_NOT_FOUND, exception.getErrorCatalog());
    }
}