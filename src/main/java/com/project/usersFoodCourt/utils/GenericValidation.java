package com.project.usersFoodCourt.utils;

import com.project.usersFoodCourt.domain.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class GenericValidation {
    public void validateCondition(boolean condition, ErrorCatalog errorCatalog) {
        if (condition) {
            throw new BusinessException(errorCatalog);
        }
    }
}
