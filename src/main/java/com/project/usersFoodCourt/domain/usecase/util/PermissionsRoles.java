package com.project.usersFoodCourt.domain.usecase.util;

import com.project.usersFoodCourt.utils.ErrorCatalog;
import com.project.usersFoodCourt.utils.GenericValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionsRoles {
    private final GenericValidation genericValidation;

    public void validateRoleCreationPermissions(String roleToCreate, String currentUserRole) {
        // Sin autenticación - solo puede crear CLIENT
        if (currentUserRole == null) {
            genericValidation.validateCondition(!"CLIENT".equals(roleToCreate),
                    ErrorCatalog.UNAUTHORIZED_ROLE_CREATION);
            return;
        }

        // Validar permisos específicos
        if ("ADMIN".equals(roleToCreate)) {
            genericValidation.validateCondition(!"ADMIN".equals(currentUserRole),
                    ErrorCatalog.ADMIN_REQUIRED_FOR_OWNER_CREATION);
        }

        if ("OWNER".equals(roleToCreate)) {
            genericValidation.validateCondition(!"ADMIN".equals(currentUserRole),
                    ErrorCatalog.ADMIN_REQUIRED_FOR_OWNER_CREATION);
        }

        if ("EMPLOYEE".equals(roleToCreate)) {
            genericValidation.validateCondition(!"OWNER".equals(currentUserRole),
                    ErrorCatalog.OWNER_REQUIRED_FOR_EMPLOYEE_CREATION);
        }
    }
}
