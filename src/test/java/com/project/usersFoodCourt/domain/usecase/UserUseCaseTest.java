package com.project.usersFoodCourt.domain.usecase;

import com.project.usersFoodCourt.domain.exception.BusinessException;
import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import com.project.usersFoodCourt.domain.spi.IRolePersistencePort;
import com.project.usersFoodCourt.domain.spi.IUserPersistencePort;
import com.project.usersFoodCourt.infrastructure.configuration.JwtService;
import com.project.usersFoodCourt.utils.ErrorCatalog;
import com.project.usersFoodCourt.utils.GenericValidation;
import com.project.usersFoodCourt.domain.usecase.util.PermissionsRoles;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
//El LENIENT permite que los stubs no utilizados no causen errores,
// lo cual es necesario cuando se tiene múltiples escenarios de test
// que requieren diferentes configuraciones de mock.
@MockitoSettings(strictness = Strictness.LENIENT)
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
    @Mock
    private GenericValidation genericValidation;
    @Mock
    private PermissionsRoles permissionsRoles;

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
        userModel.setDocumentNumber("12345678");
        userModel.setRole(roleModel);
    }

    @Test
    void registerUser_ShouldReturnAuthenticationResponse_WhenRoleExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(userPersistencePort.findByDocument("12345678")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthenticationResponse result = userUseCase.registerUserWithRoleValidation(userModel, null);

        assertNotNull(result);
        assertEquals("jwt-token", result.getAccessToken());
        verify(userPersistencePort).save(userModel);
        verify(rolePersistencePort).findByRoleId(1L);
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenRoleNotExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.empty());
        doThrow(new BusinessException(ErrorCatalog.ROLE_ID_NOT_FOUND))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.ROLE_ID_NOT_FOUND));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUserWithRoleValidation(userModel, null));

        assertEquals(ErrorCatalog.ROLE_ID_NOT_FOUND, exception.getErrorCatalog());
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenUserUnderage() {
        userModel.setBirthDate(LocalDate.now().plusDays(1));
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));
        doThrow(new BusinessException(ErrorCatalog.USER_UNDERAGE))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.USER_UNDERAGE));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUserWithRoleValidation(userModel, null));

        assertEquals(ErrorCatalog.USER_UNDERAGE, exception.getErrorCatalog());
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenEmailAlreadyExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.of(userModel));
        doThrow(new BusinessException(ErrorCatalog.USER_EMAIL_ALREADY_EXISTS))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.USER_EMAIL_ALREADY_EXISTS));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUserWithRoleValidation(userModel, null));

        assertEquals(ErrorCatalog.USER_EMAIL_ALREADY_EXISTS, exception.getErrorCatalog());
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void registerUser_ShouldThrowBusinessException_WhenDocumentAlreadyExists() {
        when(rolePersistencePort.findByRoleId(1L)).thenReturn(Optional.of(roleModel));
        when(userPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(userPersistencePort.findByDocument("12345678")).thenReturn(Optional.of(userModel));
        doThrow(new BusinessException(ErrorCatalog.USER_DOCUMENT_ALREADY_EXISTS))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.USER_DOCUMENT_ALREADY_EXISTS));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.registerUserWithRoleValidation(userModel, null));

        assertEquals(ErrorCatalog.USER_DOCUMENT_ALREADY_EXISTS, exception.getErrorCatalog());
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

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(userModel));

        UserModel result = userUseCase.getUserById(1L);

        assertNotNull(result);
        assertEquals(userModel, result);
        verify(userPersistencePort).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowBusinessException_WhenUserNotExists() {
        when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());
        doThrow(new BusinessException(ErrorCatalog.USER_NOT_FOUND))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.USER_NOT_FOUND));

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> userUseCase.getUserById(1L));

        assertEquals(ErrorCatalog.USER_NOT_FOUND, exception.getErrorCatalog());
    }
}