package com.project.usersFoodCourt.utils;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

    USER_NOT_FOUND("ERR_USER_001", "User not found."),
    INVALID_USER("ERR_USER_002", "Invalid user parameters."),
    GENERIC_ERROR("ERR_GEN_001", "An unexpected error occurred."),
    ROLE_ID_NOT_FOUND("ERR_ROLE_001", "Role not found."),
    USER_UNDERAGE("ERR_USER_003", "User must be of legal age.");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
