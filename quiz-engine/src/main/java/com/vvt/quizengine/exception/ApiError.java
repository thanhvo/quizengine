package com.vvt.quizengine.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter @Getter @NoArgsConstructor
public class ApiError {
    private HttpStatus status;

    private String message;

    @Builder
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
