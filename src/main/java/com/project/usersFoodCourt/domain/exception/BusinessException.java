package com.project.usersFoodCourt.domain.exception;

import com.project.usersFoodCourt.utils.ErrorCatalog;

public class BusinessException extends BaseException {
    public BusinessException(ErrorCatalog errorCatalog) {
        super(errorCatalog);
    }
}