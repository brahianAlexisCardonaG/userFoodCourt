package com.project.usersFoodCourt.domain.usecase.util;

import com.project.usersFoodCourt.domain.exception.BusinessException;
import com.project.usersFoodCourt.utils.ErrorCatalog;
import com.project.usersFoodCourt.utils.GenericValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionsRolesTest {

    @Mock
    private GenericValidation genericValidation;

    @InjectMocks
    private PermissionsRoles permissionsRoles;

    @Test
    void validateRoleCreationPermissions_ShouldAllowClientCreation_WhenNoAuthentication() {
        permissionsRoles.validateRoleCreationPermissions("CLIENT", null);
        
        verify(genericValidation).validateCondition(eq(false), eq(ErrorCatalog.UNAUTHORIZED_ROLE_CREATION));
    }

    @Test
    void validateRoleCreationPermissions_ShouldThrowException_WhenCreatingNonClientWithoutAuth() {
        doThrow(new BusinessException(ErrorCatalog.UNAUTHORIZED_ROLE_CREATION))
            .when(genericValidation).validateCondition(eq(true), eq(ErrorCatalog.UNAUTHORIZED_ROLE_CREATION));

        assertThrows(BusinessException.class, 
            () -> permissionsRoles.validateRoleCreationPermissions("ADMIN", null));
    }

    @Test
    void validateRoleCreationPermissions_ShouldAllowAdminCreation_WhenCurrentUserIsAdmin() {
        permissionsRoles.validateRoleCreationPermissions("ADMIN", "ADMIN");
        
        verify(genericValidation).validateCondition(eq(false), eq(ErrorCatalog.ADMIN_REQUIRED_FOR_OWNER_CREATION));
    }

    @Test
    void validateRoleCreationPermissions_ShouldAllowOwnerCreation_WhenCurrentUserIsAdmin() {
        permissionsRoles.validateRoleCreationPermissions("OWNER", "ADMIN");
        
        verify(genericValidation).validateCondition(eq(false), eq(ErrorCatalog.ADMIN_REQUIRED_FOR_OWNER_CREATION));
    }

    @Test
    void validateRoleCreationPermissions_ShouldAllowEmployeeCreation_WhenCurrentUserIsOwner() {
        permissionsRoles.validateRoleCreationPermissions("EMPLOYEE", "OWNER");
        
        verify(genericValidation).validateCondition(eq(false), eq(ErrorCatalog.OWNER_REQUIRED_FOR_EMPLOYEE_CREATION));
    }
}