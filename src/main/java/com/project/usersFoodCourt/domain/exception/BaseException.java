package com.project.usersFoodCourt.domain.exception;

import com.project.usersFoodCourt.utils.ErrorCatalog;

public abstract class BaseException extends RuntimeException {
    private final ErrorCatalog errorCatalog;

    protected BaseException(ErrorCatalog errorCatalog) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
    }

    public ErrorCatalog getErrorCatalog() {
        return errorCatalog;
    }
}