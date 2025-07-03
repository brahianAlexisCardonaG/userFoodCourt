package com.project.usersFoodCourt.domain.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;
    private LocalDate timestamp;
}
